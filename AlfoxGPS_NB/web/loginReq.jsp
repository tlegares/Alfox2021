<%-- 
    Document    : login_req
    Description : recoit par Ajax le mot de passe pour identifier et se connecter
--%>

<%@page import="java.sql.Connection"%>
<%@page import="com.persistence.*"%>

<%
    Connection con = (Connection) session.getAttribute("con");
    if (con == null)
        con = ConnexionMySQL.getInstance();
    session.setAttribute("con", con);
    // on récupère le mdp saisi
    String password = request.getParameter("mdp");
    Responsable user = Responsable.getByMotDePasse(con, password);
    // la personne existe t'elle ?
    if (user != null) {                 
        // création du user dans l'objet session de Tomcat
        session.setAttribute("user", user);
        request.getRequestDispatcher("accueil.jsp").forward(request, response);
    }
    else {      // le mot de passe n'existe pas
        request.getRequestDispatcher("index.jsp?message=pbLogin").forward(request, response);
    }
%>