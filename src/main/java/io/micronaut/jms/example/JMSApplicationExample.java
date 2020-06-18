package io.micronaut.jms.example;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

/***
 *
 * The runner class for the JMS Example Application.
 *
 * @author elliott
 */
@OpenAPIDefinition(
        info = @Info(
                title = "JMS Example Application",
                version = "1.0.0",
                description = "Example Application for building a Micronaut application with JMS",
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0"),
                contact = @Contact(name = "Elliott Pope")
        )
)
public class JMSApplicationExample {
    public static void main(String[] args) {
        Micronaut.run(JMSApplicationExample.class, args);
    }
}
