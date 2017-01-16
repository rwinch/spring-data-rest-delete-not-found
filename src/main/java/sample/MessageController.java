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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Rob Winch
 *
 */
@RestController
public class MessageController {
	private final MessageRepository messages;

	public MessageController(MessageRepository messages) {
		super();
		this.messages = messages;
	}

	@GetMapping("/messages/{id}")
	public ResponseEntity<Message> findOne(@PathVariable Long id) {
		Message body = messages.findOne(id);

		return createResponseEntity(body);
	}

	static <T> ResponseEntity<T> createResponseEntity(T body) {
		if(body == null) {
			return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<T>(body, HttpStatus.OK);
	}
}
