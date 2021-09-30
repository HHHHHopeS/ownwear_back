package com.ownwear.app.controller;

import com.clarifai.channel.ClarifaiChannel;
import com.clarifai.credentials.ClarifaiCallCredentials;
import com.clarifai.grpc.api.*;
import com.clarifai.grpc.api.status.StatusCode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@CrossOrigin(value = "*",maxAge = 3600)


public class ClarifaiController {

    @RequestMapping("/getRectorData")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public String home(@RequestBody String base64data) throws InvalidProtocolBufferException {
        base64data = base64data.substring(base64data.indexOf(",")+1);
        byte[] imagebyte = Base64.getDecoder().decode(base64data);

        V2Grpc.V2BlockingStub stub = V2Grpc.newBlockingStub(ClarifaiChannel.INSTANCE.getGrpcChannel())
                .withCallCredentials(new ClarifaiCallCredentials("a3a9c2b3df85498f8f14cfed335c8428"));

        MultiOutputResponse response = stub.postModelOutputs(
                PostModelOutputsRequest.newBuilder()
                        .setModelId("72c523807f93e18b431676fb9a58e6ad")
                        .addInputs(
                                Input.newBuilder().setData(
                                        Data.newBuilder().setImage(
                                                Image.newBuilder().setBase64(ByteString.copyFrom(imagebyte))
                                        )
                                )
                        )
                        .build()
        );

        if (response.getStatus().getCode() != StatusCode.SUCCESS) {
            throw new RuntimeException("Request failed, status: " + response.getStatus());
        }


        return JsonFormat.printer().print(response.getOutputs(0).getData().toBuilder());
    }
}