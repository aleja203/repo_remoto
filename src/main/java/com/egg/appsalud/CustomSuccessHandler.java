
package com.egg.appsalud;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
//        // Obtenemos la URL solicitada antes del inicio de sesión
//        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
//
//        if (savedRequest != null) {
//            String targetUrl = savedRequest.getRedirectUrl();
//            System.out.println("URL solicitada antes del inicio de sesión: " + targetUrl);
//
//            // Aquí podrías verificar si la URL tiene el formato adecuado y extraer el id del profesional
//            // Para obtener el id del profesional desde la URL, podrías usar métodos de manipulación de cadenas o regex
//            // Ejemplo:
//            String[] parts = targetUrl.split("/");
//            String idProfesional = parts[parts.length - 1];
//            System.out.println("ID del profesional desde la URL: " + idProfesional);
//
//            // Redirigir a la URL solicitada antes del inicio de sesión
//            getRedirectStrategy().sendRedirect(request, response, targetUrl);
//        } else {
//            // Si no hay una URL solicitada previamente, redirigir a una URL por defecto
//            System.out.println("No se encontró una URL solicitada previamente.");
//            super.onAuthenticationSuccess(request, response, authentication);
//        }
//    }

//@Override
//public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//    SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
//    if (savedRequest != null) {
//        String redirectUrl = savedRequest.getRedirectUrl();
//        System.out.println("URL solicitada antes del inicio de sesión: " + redirectUrl);
//        
//        // Realizar la redirección manualmente
//        response.sendRedirect(redirectUrl);
//    } else {
//        System.out.println("No se encontró una URL solicitada previamente.");
//    }
//}


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        if (savedRequest != null) {
            String redirectUrl = savedRequest.getRedirectUrl();
            System.out.println("URL solicitada antes del inicio de sesión: " + redirectUrl);

            // Realizar la redirección a la URL solicitada previamente
            response.sendRedirect(redirectUrl);
        } else {
            // Si no hay URL solicitada previamente, redirigir a la raíz
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}



    // Resto de tu lógica de redirección o acciones posteriores al inicio de sesión
    // ...


