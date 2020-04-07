package com.luojianhua.commu.controller;

import com.luojianhua.commu.entity.AccessToken;
import com.luojianhua.commu.entity.GithubUser;
import com.luojianhua.commu.model.User;
import com.luojianhua.commu.provider.GithubProvider;
import com.luojianhua.commu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserService userService;

    @Value("${github.client.id}")
    private String cientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state" )String state,
                           HttpServletRequest request,
                           HttpServletResponse response){

        AccessToken accessToken=new AccessToken();

        accessToken.setCode(code);
        accessToken.setRedirect_uri(redirectUri);
        accessToken.setClient_id(cientId);
        accessToken.setClient_secret(clientSecret);
        accessToken.setState(state);
        String accesstoken = githubProvider.getAccessToken(accessToken);
        GithubUser githubuser = githubProvider.getUser(accesstoken);
        if(githubuser!=null&&githubuser.getId()!=null){
            User user=new User();
            String token=UUID.randomUUID().toString();
          user.setToken(token);
          user.setName(githubuser.getName());
          user.setAccountId(String.valueOf(githubuser.getId()));
          user.setAvatarUrl(githubuser.getAvatarUrl());
          userService.createOrUpdate(user);
          response.addCookie(new Cookie("token",token));

            return "redirect:/";

        }else{
            //登陆失败，重新登陆
            return "redirect:/";
        }




    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        //删除session
        request.getSession().removeAttribute("user");
        //删除cookie
       Cookie cookie=new Cookie("token",null);
       cookie.setMaxAge(0);
       response.addCookie(cookie);
        return "redirect:/";
    }

}
