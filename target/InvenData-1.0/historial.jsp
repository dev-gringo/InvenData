<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>InvenData - Historial</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
            <div class="container">
                <a class="navbar-brand" href="#">📊 InvenData</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav me-auto">
                        <li class="nav-item">
                            <a class="nav-link" href="ProductoServlet">Inventario</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" href="MovimientoServlet">Historial</a>
                        </li>
                    </ul>
                    <div class="d-flex align-items-center">
                        <span class="navbar-text me-3 text-white">
                            👤 <strong>${sessionScope.usuarioLogueado.nombreCompleto}</strong>
                        </span>
                        <a href="LoginServlet?accion=logout" class="btn btn-outline-danger btn-sm">Cerrar Sesión</a>
                    </div>
                </div>
            </div>
        </nav>

        <div class="container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2>📜 Historial de movimientos</h2>
                <span class="badge bg-secondary">Registros de movimientos</span>
            </div>

            <table class="table table-striped table-hover shadow-sm border">
                <thead class="table-dark">
                    <tr>
                        <th>Fecha/Hora</th>
                        <th>Producto</th>
                        <th>Tipo</th>
                        <th>Cantidad</th>
                        <th>Responsable (Usuario)</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="m" items="${historial}">
                        <tr>
                            <td>
                               <fmt:formatDate value="${m.fecha}" pattern="dd/MM/yyyy - hh:mm:ss a" />
                            </td>
                            <td><strong>${m.nombreProducto}</strong></td>
                            <td>
                                <span class="badge ${m.tipo == 'ENTRADA' ? 'bg-info text-dark' : 'bg-warning text-dark'}">
                                    ${m.tipo}
                                </span>
                            </td>
                            <td class="fw-bold">${m.cantidad}</td>
                            <td>${m.nombreUsuario}</td>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty historial}">
                        <tr>
                            <td colspan="5" class="text-center text-muted">No se han registrado movimientos todavía.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>