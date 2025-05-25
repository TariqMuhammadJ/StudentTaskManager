package com.studytask.listeners;

import java.util.Optional;

import com.studytask.models.User;
import com.studytask.services.ServiceFactory;
import com.studytask.services.UserService;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {
	private final UserService userService = ServiceFactory.getUserService();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("Session Created: ID = " + se.getSession().getId());
    }

    @Override 
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();

        Object obj = session.getAttribute("user");

        if (obj instanceof Optional<?>) {
            Optional<?> opt = (Optional<?>) obj;

            if (opt.isPresent() && opt.get() instanceof User) {
                User user = (User) opt.get();

                user.setIsActive(false);
                try {
                    userService.updateIsActive(Optional.of(user));
                    System.out.println("User set to inactive and removed: ID = " + user.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
