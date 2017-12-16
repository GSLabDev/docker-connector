package org.mule.modules.docker.automation.unit;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Info;

public class TestDataBuilder {

    static ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static Info getDockerInfo() throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {

        return mapper.readValue(new FileReader("src/test/resources/input-files/info.json"), Info.class);
    }

    public static InspectContainerResponse getInspectContainerResponse() throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {
        return mapper.readValue(new FileReader("src/test/resources/input-files/inspect-container.json"), InspectContainerResponse.class);

    }

    public static List<Container> getListContainerResponse() throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {
        Container container = mapper.readValue(new FileReader("src/test/resources/input-files/list-container.json"), Container.class);
        List<Container> listContainerResponse = new ArrayList<Container>();
        listContainerResponse.add(container);
        return listContainerResponse;
    }

    public static CreateContainerResponse getCreateContainerResponse() {
        CreateContainerResponse createContainerResponse = new CreateContainerResponse();
        createContainerResponse.setId("67a02d0d8c4801657bb715964065e54740a504caae7bba270d260377277f117a");
        return createContainerResponse;
    }

    public static List<Image> getListImageResponse() throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {
        Image image = mapper.readValue(new FileReader("src/test/resources/input-files/list-image-response.json"), Image.class);
        List<Image> listImageResponse = new ArrayList<Image>();
        listImageResponse.add(image);
        return listImageResponse;
    }

    public static InspectImageResponse getInspectImageResponse() throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {
        return mapper.readValue(new FileReader("src/test/resources/input-files/inspect-image-response.json"), InspectImageResponse.class);
    }

}
