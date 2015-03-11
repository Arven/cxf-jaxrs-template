# JAX-RS Service Template for Java EE 6

### JSON Postman Tests
Postman tests using the JSON format are located in the postman
directory. These were tested with the Jackson JSON bindings
with the JAXB annotations.

### XML Postman Tests
Postman tests using the XML format for requests and responses
are located in the postman directory. These were tested with
the standard JAXB Reference Implementation bindings.

### Using JAXB for JSON
There is no standard annotations which may be used to annotate
JSON mapped data at this time. However, Jackson, Jettison,
Genson, as well as a few others will support JAXB to some
degree.

# Persistence Framework

### JPA 2.0
The Java Persistence API is a high level database connectivity
API, or Object-Relational Mapping software. With a representation
of your database as Java classes, you can annotate them and use
the persistence API to query and store data. It supports other
databases like Redis which are not SQL based.

### JDBC
JDBC is the lower level database connectivity API that is used
by JPA as well as all Java database applications.