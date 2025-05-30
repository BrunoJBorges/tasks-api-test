package br.pr.brunoborges.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class ApiTest {
	
	@BeforeClass
	public static void setUp() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend";
	}
	
	@Test
	public void deveRetornarTarefas() {
		RestAssured.
		given()
		.when()
			.get("/todo")
		.then()
			.statusCode(200);
	}
	
	@Test
	public void deveAdiconarTarefaComSucesso() {
		RestAssured.
		given()
			.body("{\"task\": \"Teste via API\", \"dueDate\": \"2030-12-30\"}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201);
	}
	
	@Test
	public void naoDeveAdiconarTarefaInvalida() {
		RestAssured.
		given()
			.body("{\"task\": \"Teste via API\", \"dueDate\": \"2010-12-30\"}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(400)
			.body("message", CoreMatchers.is("Due date must not be in past"));
	}
	
	@Test
	public void deveRemoverTarefaComSucesso() {	
		//Insere a tarefa
		Integer id = RestAssured.
		given()
			.body("{\"task\": \"Teste teste\", \"dueDate\": \"2030-12-30\"}")
			.contentType(ContentType.JSON)
		.when()
			.post("/todo")
		.then()
			.statusCode(201)
			.extract().path("id");
		
		//Remove a tarefa
		RestAssured.
		given()
		.when()
			.delete("/todo/"+id)
		.then()
			.statusCode(204);
	}

}
