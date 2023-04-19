package com.example.worker.DB;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class schemaCheck {
    private final String Json;
    private final File SchemaFile;

    public schemaCheck(String json, File schemaFile) {
        Json = json;
        SchemaFile = schemaFile;
    }

    public boolean checkJson(){
        try {
            String schema = readFileAsString();

            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
            JsonSchema validator = factory.getSchema(schema);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(Json);

            Set<ValidationMessage> errors = validator.validate(jsonNode);

            return errors.isEmpty();

        } catch (IOException e) {
            return false;
        }
    }
    public String readFileAsString() {
        try {
            String path = SchemaFile.getPath();
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
