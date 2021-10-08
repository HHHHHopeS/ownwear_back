package com.ownwear.app.controller;

import com.google.cloud.storage.*;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.sf.json.*;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.protobuf.util.*;

import java.util.Base64;
import java.util.List;

@RestController
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
    static final String storage_bucket_name =  "image-post";
    static final String projectId= "top-branch-323307";
    static final String computeRegion = "asia-east1";
    static final String productSetId = "product_set3";
    static final String productCategory = "apparel-v2";
    @RequestMapping("/getGoogleData")
    private static JSONArray getImageData(@RequestBody String base64List) throws Exception {
        List<String> response = new ArrayList<String>();

        JSONObject obj = JSONObject.fromObject(base64List);
        List<String> array = (List<String>)JSONArray.fromObject(obj.get("list"));

        for(String base64Data : array){
            String base64Byte = base64Data.substring(base64Data.indexOf(",")+1);
            byte[] imagebyte = Base64.getDecoder().decode(base64Byte);
            response.add(getSimilarProducts(projectId,computeRegion,productSetId,productCategory,imagebyte));
        }

        return JSONArray.fromObject(response);
    }
    @RequestMapping("/getList")
    private static void getList() throws IOException{
        String projectId= "top-branch-323307";
        String computeRegion = "asia-east1";
        listProducts(projectId,computeRegion);
    }

    private static  void deleteProductSet(String projectId, String computeRegion,String productSetId) throws IOException{
        try (ProductSearchClient client = ProductSearchClient.create()) {

            // Get the full path of the product set.
            String formattedName =
                    ProductSearchClient.formatProductSetName(projectId, computeRegion, productSetId);
            // Delete the product set.
            client.deleteProductSet(formattedName);
//            //System.out.println(String.format("Product set deleted"));
        }
    }
    private static void listProducts(String projectId, String computeRegion) throws IOException {
        try (ProductSearchClient client = ProductSearchClient.create()) {

            // A resource that represents Google Cloud Platform location.
            String formattedParent = ProductSearchClient.formatLocationName(projectId, computeRegion);

            // List all the products available in the region.
            for (Product product : client.listProducts(formattedParent).iterateAll()) {
                // Display the product information
//                //System.out.println(product.toString());
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
//            //System.out.println("Product deleted.");
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
//            //System.out.println(requests);
            BatchAnnotateImagesResponse response = queryImageClient.batchAnnotateImages(requests);
//            //System.out.println(response);

            return JsonFormat.printer().print(response);

        }

    }

    @RequestMapping("/uploadImageFile")
    public static JSONObject uploadImage(@RequestBody String base64data) throws  IOException{
        String filetype = base64data.substring(base64data.indexOf("/")+1,base64data.indexOf(";"));
        String base64Byte = base64data.substring(base64data.indexOf(",")+1);
        byte[] imagebyte = Base64.getDecoder().decode(base64Byte);
        long millis = System.currentTimeMillis();
        LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
        String pathprefix = now.toString();
        String objectName = new StringBuilder().append("post-").append(pathprefix).append("/").append(millis).append(".").append(filetype).toString();
        JSONObject obj = JSONObject.fromObject("{'data':'"+uploadObject(projectId,storage_bucket_name,objectName,imagebyte)+"'}");
        return obj;


//

    }
    private static String uploadObject(String projectId,String bucketName, String objectName , byte[] imgFile) throws IOException{
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        BlobId blobId = BlobId.of(bucketName,objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        try {
            storage.create(blobInfo, imgFile);
            return "https://storage.googleapis.com/image-post"+objectName;
        } catch (StorageException e) {
            e.printStackTrace();
            return null;
        }

    }




}