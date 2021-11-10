package br.edu.utfpr.editorartigos.service.impl;

import br.edu.utfpr.editorartigos.exception.UsuarioJaExisteException;
import br.edu.utfpr.editorartigos.model.Usuario;
import br.edu.utfpr.editorartigos.repository.UsuarioRepository;
import br.edu.utfpr.editorartigos.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl extends CrudServiceImpl<Usuario, Long> implements UsuarioService, UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public JpaRepository<Usuario, Long> getRepository() {
        return usuarioRepository;
    }

    @Override
    public Usuario criarUsuario(Usuario usuario) throws Exception {
        return save(usuario);
    }

    @Override
    public void valid(Usuario entity) throws UsuarioJaExisteException {
        if (usuarioRepository.findUsuarioByUsername(entity.getUsername()).isPresent())
            throw new UsuarioJaExisteException("Usuario " + entity.getUsername() + " já existe");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findUsuarioByUsername(username);
        if (usuario.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        return usuario.get();
    }
}
