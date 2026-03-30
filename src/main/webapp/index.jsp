<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
    <meta charset="UTF-8">
    <title>InvenData - Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="css/login.css">
</head>
    <body>
    <div class="login-container"> 
        <div class="text-center mb-4">
            <h2 class="display-6">🔐 InvenData</h2>
            <p class="opacity-75">Ingresa tus credenciales</p>
        </div>

        <% if(request.getParameter("error") != null) { %>
            <div class="alert alert-danger bg-danger text-white border-0 text-center py-2 mb-3">
                <i class="bi bi-exclamation-triangle-fill"></i> Usuario o contraseña incorrectos
            </div>
        <% } %>

        <form action="LoginServlet" method="POST">
            <div class="mb-3">
                <label class="form-label small fw-bold">Nombre de Usuario</label>
                <div class="input-group">
                    <span class="input-group-text bg-transparent border-end-0 text-white opacity-75">
                        <i class="bi bi-person"></i>
                    </span>
                    <input type="text" name="txtUser" class="form-control border-start-0" placeholder="Ej: admin" required>
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label small fw-bold">Contraseña</label>
                <div class="input-group">
                    <span class="input-group-text bg-transparent border-end-0 text-white opacity-75">
                        <i class="bi bi-lock"></i>
                    </span>
                    <input type="password" name="txtPass" class="form-control border-start-0" placeholder="****" required>
                </div>
            </div>
            <button type="submit" class="btn btn-primary w-100 py-2 mt-2 shadow">Ingresar</button>
        </form>
    </div>
</body>
</html>