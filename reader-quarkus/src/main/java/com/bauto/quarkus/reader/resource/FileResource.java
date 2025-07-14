package com.bauto.quarkus.reader.resource;

import com.bauto.quarkus.reader.service.FileProcessingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/reader")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class FileResource {

    @Inject
    FileProcessingService service;

    @POST
    @Path("/trigger")
    public Response trigger(@QueryParam("filepath") String filepath) {
        if (filepath == null || filepath.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Missing required query param: filepath").build();
        }

        service.processFile(java.nio.file.Path.of(filepath));
        return Response.ok("Triggered for: " + filepath).build();
    }
}
