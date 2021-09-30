package com.ownwear.app.controller;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.sf.json.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.protobuf.util.*;

import java.util.Base64;
import java.util.List;

@RestController
@CrossOrigin(value = "*",maxAge = 3600)
public class VisionController {
    static List<String> list = new ArrayList<String>();
//    @RequestMapping("/delete")
//    private static void delete(){
//        String projectId= "top-branch-323307";
//        String computeRegion = "asia-east1";
//        String productSetId = "product_set3";
//        try {
//            deleteProductSet(projectId,computeRegion,productSetId);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @RequestMapping("/getGoogleData")
    private static JSONArray getImageData(@RequestBody String base64List) throws Exception {
        List<String> response = new ArrayList<String>();
        String projectId= "top-branch-323307";
        String computeRegion = "asia-east1";
        String productSetId = "product_set3";
        String productCategory = "apparel-v2";
        JSONObject obj = JSONObject.fromObject(base64List);
        List<String> array = (List<String>)JSONArray.fromObject(obj.get("list"));
//        array.toArray();

        for(String base64Data : array){
            String base64Byte = base64Data.substring(base64Data.indexOf(",")+1);
            System.out.println(base64Byte);
            byte[] imagebyte = Base64.getDecoder().decode(base64Byte);
            response.add(getSimilarProducts(projectId,computeRegion,productSetId,productCategory,imagebyte));
        }
        System.out.println(response);
//        imageUri = imageUri.substring(imageUri.indexOf(",")+1);
//        byte[] imagebyte = Base64.getDecoder().decode(imageUri);

        return JSONArray.fromObject(response);
    }
    @RequestMapping("/getList")
    private static void getList() throws IOException{
        String projectId= "top-branch-323307";
        String computeRegion = "asia-east1";
        listProducts(projectId,computeRegion);
    }
    @RequestMapping("/ajax")
    private String ajaxReturn() {
        String io = "fuck you";
        return io;
    }
    private static  void deleteProductSet(String projectId, String computeRegion,String productSetId) throws IOException{
        try (ProductSearchClient client = ProductSearchClient.create()) {

            // Get the full path of the product set.
            String formattedName =
                    ProductSearchClient.formatProductSetName(projectId, computeRegion, productSetId);
            // Delete the product set.
            client.deleteProductSet(formattedName);
            System.out.println(String.format("Product set deleted"));
        }
    }
    private static void listProducts(String projectId, String computeRegion) throws IOException {
        try (ProductSearchClient client = ProductSearchClient.create()) {

            // A resource that represents Google Cloud Platform location.
            String formattedParent = ProductSearchClient.formatLocationName(projectId, computeRegion);

            // List all the products available in the region.
            for (Product product : client.listProducts(formattedParent).iterateAll()) {
                // Display the product information
                System.out.println(product.toString());
//                list.add(product.getName().substring(product.getName().lastIndexOf('/') + 1).toString());


            }
            for(String item : list){
//                deleteProduct(projectId,computeRegion,item);
            }
        }
    }
    public static void deleteProduct(String projectId, String computeRegion, String productId)
            throws IOException {
        try (ProductSearchClient client = ProductSearchClient.create()) {

            // Get the full path of the product.
            String formattedName =
                    ProductSearchClient.formatProductName(projectId, computeRegion, productId);

            // Delete a product.
            client.deleteProduct(formattedName);
            System.out.println("Product deleted.");
        }
    }
    private static String getSimilarProducts(String projectId, String computeRegion, String productSetId, String productCategory, byte[] imgUri) throws Exception{
        try(ImageAnnotatorClient queryImageClient = ImageAnnotatorClient.create()){
            String productSetPath = ProductSetName.of(projectId,computeRegion,productSetId).toString();
            Feature featuresElement = Feature.newBuilder().setType(Feature.Type.PRODUCT_SEARCH).build();
            Image image = Image.newBuilder().setContent(ByteString.copyFrom(imgUri)).build();

            ImageContext imageContext = ImageContext.newBuilder()
                    .setProductSearchParams(
                            ProductSearchParams.newBuilder()
                                    .setProductSet(productSetPath)
                                    .addProductCategories(productCategory)

                    ).build();
            AnnotateImageRequest annotateImageRequest =
                    AnnotateImageRequest.newBuilder()
                            .addFeatures(featuresElement)
                            .setImage(image)
                            .setImageContext(imageContext)
                            .build();
            List<AnnotateImageRequest> requests = Arrays.asList(annotateImageRequest);
            System.out.println(requests);
            BatchAnnotateImagesResponse response = queryImageClient.batchAnnotateImages(requests);
            System.out.println(response);

            return JsonFormat.printer().print(response);

        }

    }
}
