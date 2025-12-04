package com.example.duan1.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ProductTypeAdapter implements JsonDeserializer<Product> {
    @Override
    public Product deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        Product product = new Product();

        // Parse các field thông thường
        if (jsonObject.has("_id")) {
            product.setId(jsonObject.get("_id").getAsString());
        }
        if (jsonObject.has("name")) {
            product.setName(jsonObject.get("name").getAsString());
        }
        if (jsonObject.has("description")) {
            product.setDescription(jsonObject.get("description").getAsString());
        }
        if (jsonObject.has("price")) {
            product.setPrice(jsonObject.get("price").getAsDouble());
        }
        if (jsonObject.has("image")) {
            product.setImage(jsonObject.get("image").getAsString());
        }
        if (jsonObject.has("quantity")) {
            product.setQuantity(jsonObject.get("quantity").getAsInt());
        }

        // Xử lý category_id có thể là object hoặc string
        if (jsonObject.has("category_id")) {
            JsonElement categoryElement = jsonObject.get("category_id");
            if (categoryElement.isJsonObject()) {
                // Nếu là object, parse thành Category và lấy id
                JsonObject categoryObj = categoryElement.getAsJsonObject();
                Category category = new Category();
                if (categoryObj.has("_id")) {
                    category.setId(categoryObj.get("_id").getAsString());
                }
                if (categoryObj.has("name")) {
                    category.setName(categoryObj.get("name").getAsString());
                }
                product.setCategory(category);
                product.setCategoryId(category.getId());
            } else if (categoryElement.isJsonPrimitive()) {
                // Nếu là string, set trực tiếp
                product.setCategoryId(categoryElement.getAsString());
            }
        }

        return product;
    }
}


