/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ferreteriacruz.Services;

/**
 *
 * @author USUARIO
 */


import ferreteriacruz.dao.UsuarioDAO;
import ferreteriacruz.entity.Usuario;

public class LoginService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario autenticar(String email, String password) {
        return usuarioDAO.login(email, password);
    }
}