# 🛠️ Sistema de Soporte Operacional – Ferretería Cruz

Proyecto académico para el desarrollo de un **Sistema de Gestión Operacional** orientado a automatizar los procesos de **ventas, inventario y control de stock** de la microempresa **Ferretería Cruz**, ubicada en el puesto D107 del Mercado *24 de Junio* – Villa El Salvador.

El sistema busca reemplazar los procesos manuales actuales (registro en cuaderno, stickers de precios y control visual de stock) por una solución digital robusta desarrollada en **Java** y **MySQL**, aplicando la **metodología ágil Scrum**.

---

## 📌 Introducción

Actualmente, la Ferretería Cruz gestiona sus operaciones de manera manual, lo que genera errores frecuentes en el control de inventario, pérdidas de información y demoras en la atención al cliente.  
Este proyecto propone el desarrollo de un **Sistema de Soporte Operacional** que permita automatizar los procesos críticos del negocio, mejorar la eficiencia operativa y facilitar la toma de decisiones mediante información confiable y en tiempo real.

---

## 🏪 Contexto del Negocio

Ferretería Cruz es una microempresa dedicada a la comercialización de artículos de ferretería de marcas reconocidas como **Dewalt, Stanley y Truper**.  

### Actores principales:
- **Vendedora:** realiza la venta directa y verifica el stock de forma visual.
- **Dueño:** gestiona el reabastecimiento y compras a proveedores.

### Problema operativo actual:
- Registro manual de ventas en cuaderno.
- Precios identificados mediante stickers físicos.
- Control de stock empírico (“al ojo”).
- Reabastecimiento basado en reportes verbales.

---

## ❗ Planteamiento del Problema

### Problema Central
La falta de un sistema automatizado genera **ineficiencia operativa y vulnerabilidad en la información comercial**, afectando el control del inventario, el cierre de caja y la continuidad del negocio.

### Causas
- Ausencia de herramientas digitales.
- Dependencia total del factor humano.
- Desactualización tecnológica.

### Efectos
- Quiebres de stock y pérdida de ventas.
- Errores en el cierre de caja.
- Pérdida y deterioro de información histórica.

---

## 🎯 Objetivos del Proyecto

### Objetivo General
Desarrollar e implementar un **Sistema de Soporte Operacional** para la Ferretería Cruz utilizando **Java** y **MySQL**, que automatice los procesos de ventas e inventario.

### Objetivos Específicos
- Digitalizar los registros de ventas e inventario.
- Automatizar el cálculo de ventas y control de stock.
- Implementar alertas de stock mínimo.
- Optimizar la atención al cliente eliminando el uso de stickers físicos.
- Aplicar Scrum y control de versiones durante el desarrollo.

---

## 📦 Alcance del Proyecto

### ✔️ Incluye
- Aplicación de escritorio en Java.
- Base de datos relacional en MySQL.
- Gestión de productos, categorías y marcas.
- Módulo de ventas y cierre de caja.
- Gestión de usuarios con roles (Vendedor / Dueño).
- Reportes básicos de ventas y stock.
- Desarrollo bajo metodología Scrum (4 sprints).

### ❌ No Incluye
- Facturación electrónica SUNAT.
- Tienda virtual o pagos online.
- Lectores de código de barras (ingreso manual).

---

## 🔄 Metodología de Trabajo – Scrum

El proyecto se desarrolla utilizando **Scrum**, con entregas incrementales y sprints de 2 semanas.

### Roles
- **Product Owner:** Representa las necesidades de la Ferretería Cruz.
- **Scrum Master:** Facilita el proceso ágil y elimina impedimentos.
- **Development Team:** Diseña, programa y documenta el sistema.

### Eventos
- Sprint Planning  
- Daily Scrum  
- Sprint Review  
- Sprint Retrospective  

### Artefactos
- Product Backlog  
- Sprint Backlog  
- Incremento de software funcional  

---

## 🗓️ Planificación del Proyecto (Sprints)

| Sprint | Objetivo |
|------|---------|
| Sprint 1 | Configuración del entorno, Git y módulo de productos |
| Sprint 2 | Gestión de inventario y control de stock |
| Sprint 3 | Módulo de ventas y clientes |
| Sprint 4 | Reportes, cierre de caja y despliegue |

---

## ⚙️ Sprint 1 – Historias Técnicas

- Configuración del repositorio Git con estructura profesional.
- Definición de estrategia de ramas y convención de commits.
- Protección de la rama `main`.
- Uso de Pull Requests.
- Configuración de plantillas de Issues y PR.
- Repositorio remoto en GitHub.

---

## 👤 Historias de Usuario – Sprint 1 (Inventario Base)

Ejemplos:
- Registrar productos con nombre, precio y SKU.
- Consultar listado de productos con búsqueda.
- Editar y desactivar productos.
- Actualizar stock por ingreso de mercadería.
- Alertas automáticas de stock mínimo.

---

## 🛠️ Tecnologías y Herramientas

- **Java**
- **MySQL**
- **Git & GitHub**
- **Docker**
- **IntelliJ IDEA / VS Code**
- **Trello / Jira**
- **Discord / Microsoft Teams**

---

## 🏗️ Arquitectura del Sistema

Arquitectura en capas:
- **Presentación:** Interfaz gráfica (GUI).
- **Lógica de Negocio:** Servicios en Java.
- **Persistencia:** MySQL.
- **Control de Acceso:** Roles y usuarios.

---

## 📈 Resultados Esperados

- Automatización total de ventas e inventario.
- Reducción de errores humanos.
- Mejor control del stock y caja diaria.
- Software escalable y mantenible.
- Aplicación real de Scrum y buenas prácticas.

---

## 🔮 Mejoras Futuras

- Integración con SUNAT.
- Implementación de lectores de códigos de barras.
- Sistema web o móvil.
- Reportes avanzados y analítica.




---

## 📚 Autores
-
-
---

## 📚 Referencias

- Sommerville, I. (2016). *Ingeniería de Software*. Pearson.
- Schwaber, K., & Sutherland, J. (2020). *The Scrum Guide*.
- Pressman, R. (2015). *Ingeniería del Software*. McGraw-Hill.
