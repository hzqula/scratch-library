package com.project.bukuproject;

import com.project.bukuproject.Model.Buku;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public class BookController {
    @FXML
    private ImageView bookImage;

    @FXML
    private Label namaBuku;

    @FXML
    private Label genre;

    @FXML
    private Label namaPenulis;

    @FXML
    private Button tombolDetail;



    public void setData(@NotNull Buku buku) {
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(buku.ImgSrc)));
            bookImage.setImage(image);
            namaBuku.setText(buku.judul);
            namaPenulis.setText(buku.penulis);
            genre.setText(String.join(", ", buku.genre));

            tombolDetail.setOnAction(event -> {
                // Handle navigasi ke halaman detail buku
                navigateToDetailPage(buku);
            });

        } catch (NullPointerException | IllegalArgumentException e) {
            e.printStackTrace(); // Handle or log the exception based on your requirements.
        }
    }



    private void navigateToDetailPage(Buku bukuSekarang) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("detail.fxml"));
            Parent root = loader.load();

            // Mendapatkan controller dari detail_page.fxml
            DetailController detailController = loader.getController();

            // Mengirim informasi buku yang dipilih ke controller detail
            detailController.setBookDetails(bukuSekarang);

            // Menampilkan halaman detail buku di stage yang sama
            Stage currentStage = (Stage) tombolDetail.getScene().getWindow();
            currentStage.setTitle("Detail Buku");
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error jika terjadi kesalahan navigasi
        }
    }

}
