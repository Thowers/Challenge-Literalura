package com.alura.literalura;

import com.alura.literalura.principal.Principal;
import com.alura.literalura.repositorio.RepositorioAutor;
import com.alura.literalura.repositorio.RepositorioLibro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private RepositorioAutor repositorioAutor;
	@Autowired
	private RepositorioLibro repositorioLibro;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repositorioLibro, repositorioAutor);
		principal.muestraMenu();
	}
}
