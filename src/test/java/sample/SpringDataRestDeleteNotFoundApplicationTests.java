/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SpringDataRestDeleteNotFoundApplicationTests {
	@Autowired
	TestRestTemplate rest;

	@Test
	public void messagesWhenId1ThenGetHelloSpringBoot() {
		long id = 100;
		Message message = rest.getForEntity("/messages/{id}", Message.class, id).getBody();

		assertThat(message).isNotNull();
		assertThat(message.getText()).isEqualTo("Hello Spring Boot!");
	}

	@Test
	public void messagesCrud() {
		Message toCreate = createMessage();

		ResponseEntity<Message> created = rest.postForEntity("/messages/", toCreate, Message.class);

		assertThat(created).isNotNull();
		assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(created.getBody().getText()).isEqualTo(toCreate.getText());

		URI createdUri = created.getHeaders().getLocation();

		Message getForEntity = rest.getForEntity(createdUri, Message.class).getBody();
		assertThat(getForEntity.getText()).isEqualTo(toCreate.getText());

		// response is 405 when @GetMapping("/messages/{id}") is present
		rest.delete(createdUri);

		assertThat(rest.getForEntity(createdUri, Message.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	static Message createMessage() {
		Message expected = new Message();
		expected.setId(1L);
		expected.setText("This is a test on " + new Date());
		return expected;
	}
}
