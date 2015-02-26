# cxf-jaxrs-template
Apache CXF JAXRS Template

### JSON Postman Tests
Postman tests using the JSON format are located in the postman
directory. These were tested with the Jackson JSON bindings
with the JAXB annotations.

### XML Postman Tests
Postman tests using the XML format for requests and responses
are located in the postman directory. These were tested with
the standard JAXB Reference Implementation bindings.

Using JAXB for JSON
===================
There is no standard annotations which may be used to annotate
JSON mapped data at this time. However, Jackson, Jettison,
Genson, as well as a few others will support JAXB to some
degree.
