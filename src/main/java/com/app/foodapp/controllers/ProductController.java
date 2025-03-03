package com.app.foodapp.controllers;


import com.app.foodapp.models.Product;
import com.app.foodapp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class ProductController {

    @Autowired
    private ProductService productService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(this.productService.getAllProducts());
    }

    @GetMapping("/{id}/")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = this.productService.getProductById(id);

        return product.map(ResponseEntity::ok).orElseGet(
                () -> ResponseEntity.notFound().build()
        );

    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") String price,
            @RequestParam("file")MultipartFile file
            ) {
        if (file.isEmpty()) {  // Comprobamos que el archivo esta en la solicitud
            return ResponseEntity.badRequest().body(null); // BadRequest es el error 400
        }

        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR)); // Si no existe el directorio, lo crea en el proyecto

            // Subo un archivo que se llama archivo1.png

            // La hora en milisegundos + _ + archivo1.png
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename(); //0832423_archivo1.png
            String filePath = UPLOAD_DIR + fileName; // uploads/0832423_archivo1.png
            file.transferTo(new File(filePath)); // Mover el archivo subido a la carpeta uploads

            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setImage(filePath); // Almacenar la ruta completa de la ubicacion del archivo

            return ResponseEntity.ok(this.productService.createProduct(product));
        }
        catch (IOException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}
