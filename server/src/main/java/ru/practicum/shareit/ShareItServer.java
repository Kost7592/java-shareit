package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Сервер приложения ShareIt
 */
@SpringBootApplication
public class ShareItServer {

	/**
	 * Точка входа в сервер приложения.
	 *
	 * @param args аргументы командной строки
	 */
	public static void main(String[] args) {
		SpringApplication.run(ShareItServer.class, args);
	}

}
