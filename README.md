# Proficienteche/RestFlow

RestFlow is a Java-based library designed to simplify the development of Restful applications, particularly in microservices architectures. With its fluent API, RestFlow empowers client applications to effortlessly interact with API endpoints on servers, reducing complexity and enhancing productivity.

## Key Features

- **Fluent API Design:** RestFlow's intuitive API design allows client applications to interact with API endpoints using just a few lines of code, making development straightforward and efficient.

- **Secure Authentication:** RestFlow supports various security schemes described in OpenAPIv3.0, including API Key, Basic Authentication, and OAuth2 client credentials grant type. It seamlessly manages authentication details, ensuring secure communication between client and server.

- **Flexible Configuration:** Client applications can configure multiple distinct Rest client instances with API endpoint details, allowing for versatile usage across different scenarios.

- **Efficient Caching:** RestFlow efficiently manages access tokens for OAuth2 client credentials grant type authentication, utilizing in-memory and Redis cache mechanisms to optimize performance, especially in clustered environments.

- **Endpoint Templates:** RestFlow enables the registration of API endpoint templates with fixed details such as path, HTTP method, and security schemes. Client applications can then dynamically fill in the remaining dynamic details and send requests to the server.

## Usage

To use RestFlow in java project, use the below maven dependency:

		<!-- Maven Dependency -->
		<dependency>
			<groupId>io.github.proficienteche</groupId>
			<artifactId>restflow</artifactId>
			<version>1.1.0</version>
		</dependency>

### RestFlow usage with code snippet

```java

public class RestFlowTest {
    public static void main(String[] args) throws JsonProcessingException {
        testRestFlow_client();
    }

    private static void testRestFlow_client() throws JsonProcessingException {

        /**
         * OAuth2.0 client credential details. Replace the values with your specific details.
         */
        String clientId = "ANcCdVEZL6Zg7Ydu9Akh2CX";
        String clientSecret = "wu0SLxWMfnjMcqWeJGmUuMyW1Q9E2agv";
        String tokenUrl = "https://jsonplaceholder.typicode.com/oauth/token";
        String scope = "readOnly";

        /**
         * Restful API app server details. Replace the values with your specific details.
         */
        String host = "https://jsonplaceholder.typicode.com";
        String context = "shipments";

        /**
         * Create Rest client instance with host, api context and Redis cache IP and Port
         * Usually a server can host multiple Restful api's. Then context is required to create unique rest client.
         * In-memory cache is default when Redis server details are not passed.
         */
        RESTClient restClient = RestFlow.createInstance(host, context, "127.0.0.1", 6379);

        /**
         * Getting OAuth2.0 client credentials builder from rest client instance.
         */

        ClientCredentialsBuilder builder = restClient.securitySchemeBuilderOf(SecureSchemeType.OAUTH_2);

        /**
         * Configure OAuth2.0 client credential details to builder and build the security scheme
         */

        builder.
                clientId(clientId).
                clientSecret(clientSecret).
                scope(scope).
                tokenUrl(tokenUrl).
                build();

        try {
            /**
             * Create endpoint template with required details.
             * As part of template configuration, expected status code and response type can be defined. Also, the
             * configuration can be registered with RestFlow to the template later and pass only dynamic details.
             */
            Tracking post = restClient.createEndpointTemplate().
                    withEndpointPath("/api/track/v1/details/{inquiryNumber}/").
                    withMethod(Http.Method.GET).
                    withPathParameter("inquiryNumber", "1Z2220060290602143").
                    withQueryParameter("status", "found").
                    withHeader("transId", "testing Id").
                    withHeader("transactionSrc", "testing").
                    withExpectedStatus(Http.Status.OK).
                    withResponseType(Tracking.class).
                    withTimeout(30).
                    createEndpoint().
                    dispatch();

            /**
             * Client app process the response
             */
            System.out.println("Response processing " + post.toString());

            /**
             * If http response status code is not matched with expected status, unable to serialize the response
             * into response type, RestFlow throws APIResponseException which contains all the details sent by API
             * server. The APIResponseException object can be used to handle the unexpected response from server.
             */
        } catch (APIResponseException e) {
            System.out.println("Error -> \n" + e.getMessage());
        }


        try {
            /**
             * If user does not know what is the endpoint http response status code and response type, then RestFlow
             * returns default response type 'EndpointResponse' which contains status code and response body.
             *
             * returns EndpointResponse
             */
            EndpointResponse endpointResponse = restClient.createEndpointTemplate().
                    withEndpointPath("/api/track/v1/details/{inquiryNumber}/").
                    withMethod(Http.Method.GET).
                    withPathParameter("inquiryNumber", "1Z2220060290602143").
                    withQueryParameter("status", "found").
                    withHeader("transId", "testing Id").
                    withHeader("transactionSrc", "testing").
                    withTimeout(30).
                    createEndpoint().
                    dispatch();

        } catch (APIResponseException e) {
            throw new RuntimeException(e);
        }

    }
}
 ```   
## Contributing
We welcome contributions to RestFlow! To contribute, please follow these steps:

- Fork the repository.
- Create a new branch for your feature or bug fix.
- Make your changes and commit them.
- Push your changes to your fork.
- Submit a pull request with a detailed description of your changes.

## License

RestFlow is released under the [MIT License](LICENSE). See the [LICENSE](LICENSE) file for more details.