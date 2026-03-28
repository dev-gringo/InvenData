<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Forzamos que la respuesta y la página usen UTF-8 --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %> 
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8"> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>InvenData - Gestión de Inventario</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        
     <style>
    @media print {
        /* Esconder navbars, botones, modales y el buscador al imprimir */
        .navbar, .btn, .modal, .row.mb-4, .row.mb-3, .alert, th:last-child, td:last-child {
            display: none !important;
        }
        
        /* Ajustar la tabla para que ocupe toda la hoja */
        .container {
            width: 100% !important;
            max-width: none !important;
            margin: 0 !important;
            padding: 0 !important;
        }
        
        .table {
            width: 100% !important;
            border: 1px solid #000 !important;
        }

        /* Añadir un título que solo se vea al imprimir */
        .print-header {
            display: block !important;
            text-align: center;
            margin-bottom: 20px;
        }
    }

    /* Esconder el título de impresión en la pantalla normal */
    .print-header {
        display: none;
    }
    </style>
    
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
                            <a class="nav-link active" href="ProductoServlet">Inventario</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="MovimientoServlet">Historial</a>
                        </li>
                    </ul>
                    <div class="d-flex align-items-center">
                        <span class="navbar-text me-3 text-white">
                            Bienvenido, <strong>${sessionScope.usuarioLogueado.nombreCompleto}</strong>
                        </span>
                        <a href="LoginServlet?accion=logout" class="btn btn-outline-danger btn-sm">Cerrar Sesión</a>
                    </div>
                </div>
            </div>
        </nav>

        <div class="container">
             
     <div class="row align-items-center mt-4 mb-2">
    <div class="col-md-3 no-print"></div>
    
    <div class="col-md-6 text-center">
        <h2 class="display-6 fw-bold mb-0">📦 Panel de Inventario</h2>
        
        <div class="d-none d-print-block mt-2">
            <h2>InvenData - Reporte de Inventario</h2>
            <small class="text-muted">
                Reporte generado el: <%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()) %>
            </small>
        </div>
    </div>

    <div class="col-md-3 text-end no-print">
        <button onclick="window.print();" class="btn btn-secondary shadow-sm">
            🖨️ Imprimir Reporte
        </button>
    </div>
    
    <div class="col-12 mt-2">
        <hr class="w-25 mx-auto">
    </div>
</div>
            
    <div class="row mb-4">
       <div class="col-md-4">
           <a href="ProductoServlet?accion=verCriticos" class="text-decoration-none">
               <div class="card bg-light border-danger shadow-sm h-100 btn-outline-danger" style="transition: 0.3s; cursor: pointer;">
                   <div class="card-body text-center">
                       <h6 class="card-title text-muted">Productos en Alerta</h6>

                       <h2 class="display-6 fw-bold ${totalAlertas > 0 ? 'text-danger' : 'text-success'}">
                           ${totalAlertas}
                       </h2>

                       <c:choose>
                           <c:when test="${totalAlertas > 0}">
                               <small class="text-danger fw-bold">⚠️ Requieren reposición inmediata</small>
                           </c:when>
                           <c:otherwise>
                               <small class="text-success">✅ Stock saludable</small>
                           </c:otherwise>
                       </c:choose>
                   </div>
               </div>
           </a>
       </div>
   </div>

            <c:if test="${param.error == 'insuficiente'}">
                <div class="alert alert-danger">❌ Error: Stock insuficiente para realizar la salida.</div>
            </c:if>
            <c:if test="${param.msj == 'exito'}">
                <div class="alert alert-success">✅ Movimiento registrado correctamente.</div>
            </c:if>

            <button type="button" class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#modalProducto" onclick="prepararNuevo()">
                + Nuevo Producto
            </button>
                
 <div class="row mb-3 mt-2">
    <div class="col-md-6">
        <form action="ProductoServlet" method="GET" class="d-flex">
            <input type="hidden" name="accion" value="buscar">
            <input type="text" name="txtBusqueda" class="form-control me-2" placeholder="Buscar producto por nombre...">
            <button type="submit" class="btn btn-dark">🔍</button>
        </form>
    </div>
    
    <div class="col-md-6 text-end">
        <a href="ProductoServlet?accion=verCriticos" class="btn btn-outline-danger">
            ⚠️ Ver Stock Crítico
        </a>
        <a href="ProductoServlet" class="btn btn-outline-secondary">
            🔄 Ver Todo
        </a>
    </div>
</div>       

            <table class="table table-hover table-bordered shadow-sm">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Categoría</th>
                        <th>Precio</th>
                        <th>Stock Actual</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${productos}">
                        <tr>
                            <td>${p.id}</td>
                            <td>${p.nombre}</td>
                            <td>${p.categoria}</td>
                            <td>$ ${p.precio}</td>
                            <td>
                                <span class="badge ${p.stock <= p.stockMinimo ? 'bg-danger' : 'bg-success'}">
                                    ${p.stock} (Min: ${p.stockMinimo})
                                </span>
                            </td>
                            <td>
                                <div class="btn-group">
                                    <button type="button" class="btn btn-warning btn-sm" 
                                            onclick="prepararMovimiento(${p.id}, '${p.nombre}')" 
                                            data-bs-toggle="modal" data-bs-target="#modalMovimiento">
                                        ±
                                    </button>
                                    
                                    <button type="button" class="btn btn-info btn-sm text-white" 
                                            onclick="llenarModalEditar(${p.id}, '${p.nombre}', '${p.categoria}', ${p.precio}, ${p.stockMinimo})" 
                                            data-bs-toggle="modal" data-bs-target="#modalProducto">
                                        ✏️
                                    </button>

                                    <a href="ProductoServlet?accion=eliminar&id=${p.id}" 
                                       class="btn btn-danger btn-sm" 
                                       onclick="return confirm('¿Seguro que deseas eliminar este producto?')">
                                        🗑️
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="modal fade" id="modalProducto" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="ProductoServlet" method="POST" accept-charset="UTF-8">
                        <div class="modal-header" id="headerProd">
                            <h5 class="modal-title" id="tituloModalProd">Agregar Nuevo Producto</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" name="txtId" id="prod_id">
                            
                            <label class="form-label">Nombre</label>
                            <input type="text" name="txtNombre" id="prod_nom" class="form-control mb-2" required>
                            
                            <label class="form-label">Categoría</label>
                            <input type="text" name="txtCategoria" id="prod_cat" class="form-control mb-2" required>
                            
                            <label class="form-label">Precio</label>
                            <input type="number" step="0.01" name="txtPrecio" id="prod_pre" class="form-control mb-2" required>
                            
                            <div id="divStock">
                                <label class="form-label">Stock Inicial</label>
                                <input type="number" name="txtStock" id="prod_sto" class="form-control mb-2">
                            </div>
                            
                            <label class="form-label">Stock Mínimo (Alerta)</label>
                            <input type="number" name="txtStockMin" id="prod_min" class="form-control mb-2" required>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" name="accion" id="btnAccionProd" value="guardar" class="btn btn-success w-100">Guardar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="modal fade" id="modalMovimiento" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="MovimientoServlet" method="POST" accept-charset="UTF-8">
                        <div class="modal-header bg-warning">
                            <h5 class="modal-title" id="tituloMov">Registrar Movimiento</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" name="txtIdProducto" id="idProdMov">
                            <div class="mb-3">
                                <label class="form-label">Tipo de Movimiento</label>
                                <select name="txtTipo" class="form-select">
                                    <option value="ENTRADA">ENTRADA (Aumentar Stock)</option>
                                    <option value="SALIDA">SALIDA (Disminuir Stock)</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Cantidad</label>
                                <input type="number" name="txtCantidad" class="form-control" min="1" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-dark">Procesar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        
        <script>
            function prepararNuevo() {
                document.getElementById('tituloModalProd').innerText = "Agregar Nuevo Producto";
                document.getElementById('btnAccionProd').value = "guardar";
                document.getElementById('btnAccionProd').innerText = "Guardar Producto";
                document.getElementById('btnAccionProd').className = "btn btn-success w-100";
                document.getElementById('divStock').style.display = "block";
                document.getElementById('prod_id').value = "";
                document.querySelector('#modalProducto form').reset();
            }

            function llenarModalEditar(id, nom, cat, pre, min) {
                document.getElementById('tituloModalProd').innerText = "Editar Producto: " + nom;
                document.getElementById('btnAccionProd').value = "editar";
                document.getElementById('btnAccionProd').innerText = "Actualizar Cambios";
                document.getElementById('btnAccionProd').className = "btn btn-info text-white w-100";
                document.getElementById('divStock').style.display = "none"; 
                
                document.getElementById('prod_id').value = id;
                document.getElementById('prod_nom').value = nom;
                document.getElementById('prod_cat').value = cat;
                document.getElementById('prod_pre').value = pre;
                document.getElementById('prod_min').value = min;
            }

            function prepararMovimiento(id, nombre) {
                document.getElementById('idProdMov').value = id;
                document.getElementById('tituloMov').innerText = "Movimiento para: " + nombre;
            }
        </script>
    </body>
</html>