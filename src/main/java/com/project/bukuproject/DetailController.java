package com.project.bukuproject;

import com.project.bukuproject.Model.Buku;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class DetailController implements Initializable {


    @FXML
    private ImageView bookImage;

    @FXML
    private Label genre;

    @FXML
    private Label judul;

    @FXML
    private Label penulis;

    @FXML
    private Label sinopsis;

    @FXML
    private GridPane penulisBookContainer;

    @FXML
    private GridPane genreBookContainer;

    @FXML
    private GridPane sinopsisBookContainer;

    @FXML
    private Button homeButton; //mengubah yang sebelumnya backButton

    private final Set<String> daftarRekomendasiDitampilkan = new HashSet<>();
    private final Set<String> daftarGenreDitampilkan = new HashSet<>();
    private final Set<String> daftarPenulisDitampilkan = new HashSet<>();


    @FXML
    private void goBack() {
        // Handle the "Kembali" button action
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();

            // Set up the controller or pass any necessary data

            // Switch to the previous page
            Stage currentStage = (Stage) homeButton.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error jika terjadi kesalahan navigasi
        }
    }

    public void setBookDetails(Buku bukuSekarang) {
        System.out.println("Setting book details for: " + bukuSekarang.judul);
        // Mengatur informasi buku pada halaman detail
        judul.setText(bukuSekarang.judul);
        genre.setText(Arrays.toString(bukuSekarang.genre));
        penulis.setText(bukuSekarang.penulis);
        sinopsis.setText(bukuSekarang.sinopsis);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(bukuSekarang.ImgSrc)));
        bookImage.setImage(image);
        daftarRekomendasiDitampilkan.add(bukuSekarang.judul);
        daftarGenreDitampilkan.add(bukuSekarang.judul);
        daftarPenulisDitampilkan.add(bukuSekarang.judul);

        // Pemanggilan metode rekomendasi untuk mendapatkan buku dengan genre yang sama
        bukuSekarang.rekomendasi(Buku.daftarBuku);

        // Menampilkan buku rekomendasi pada halaman detail
        tampilkanRekomendasiBuku(bukuSekarang.bukuTerhubungRekomendasi);

        // Pemanggilan metode cekGenreDFS untuk mendapatkan buku dengan genre yang sama
        bukuSekarang.cekGenre(Buku.daftarBuku);

        // Menampilkan buku dengan genre yang sama pada halaman detail
        tampilkanBukuSamaGenre(bukuSekarang.bukuTerhubungGenre);

        // Pemanggilan metode cekPenulisDFS untuk mendapatkan buku dengan genre yang sama
        bukuSekarang.cekPenulis(Buku.daftarBuku);

        // Menampilkan buku dengan penulis yang sama pada halaman detail
        tampilkanBukuSamaPenulis(bukuSekarang.bukuTerhubungPenulis);

    }


    private void tampilkanRekomendasiBuku(List<Buku> bukuRekomendasi) {
        // Membersihkan konten sebelum menambahkan buku baru
        sinopsisBookContainer.getChildren().clear();

        if (bukuRekomendasi != null && !bukuRekomendasi.isEmpty()) {
            int column = 0;
            int row = 1;

            // Menambahkan buku-buku dengan genre yang sama
            for (Buku buku : bukuRekomendasi) {
                // Periksa apakah judul buku sudah ditampilkan sebelumnya
                if (!daftarRekomendasiDitampilkan.contains(buku.judul)) {
                    // Menggunakan komponen FXML atau JavaFX untuk menampilkan informasi buku
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("book.fxml"));
                    try {
                        VBox bookBox = fxmlLoader.load();
                        BookController bookController = fxmlLoader.getController();
                        bookController.setData(buku);

                        // Menambahkan buku ke gridPane
                        sinopsisBookContainer.add(bookBox, column++, row);

                        GridPane.setMargin(bookBox, new Insets(10));

                        // Menyimpan judul buku yang sudah ditampilkan
                        daftarRekomendasiDitampilkan.add(buku.judul);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Handle error jika terjadi kesalahan dalam memuat komponen buku
                    }
                }
            }
        } else {
            Label label = new Label("Tidak Ada Buku dengan Sinopsis yang Sama");
            sinopsisBookContainer.add(label, 0, 0);
        }
    }


    private void tampilkanBukuSamaGenre(List<Buku> bukuSamaGenre) {
        // Membersihkan konten sebelum menambahkan buku baru
        genreBookContainer.getChildren().clear();

        if (bukuSamaGenre != null && !bukuSamaGenre.isEmpty()) {
            int column = 0;
            int row = 1;

            // Menambahkan buku-buku dengan genre yang sama
            for (Buku buku : bukuSamaGenre) {
                // Periksa apakah judul buku sudah ditampilkan sebelumnya
                if (!daftarGenreDitampilkan.contains(buku.judul)) {
                    // Menggunakan komponen FXML atau JavaFX untuk menampilkan informasi buku
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("book.fxml"));
                    try {
                        VBox bookBox = fxmlLoader.load();
                        BookController bookController = fxmlLoader.getController();
                        bookController.setData(buku);

                        // Menambahkan buku ke gridPane
                        genreBookContainer.add(bookBox, column++, row);

                        GridPane.setMargin(bookBox, new Insets(10));

                        // Menyimpan judul buku yang sudah ditampilkan
                        daftarGenreDitampilkan.add(buku.judul);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Handle error jika terjadi kesalahan dalam memuat komponen buku
                    }
                }
            }
        } else {
            // Menambahkan label untuk menunjukkan bahwa tidak ada buku dengan genre yang sama
            Label label = new Label("Tidak Ada Buku dengan Genre yang Sama");
            genreBookContainer.add(label, 0, 0);
        }
    }

    private void tampilkanBukuSamaPenulis(List<Buku> bukuSamaPenulis) {
        // Membersihkan konten sebelum menambahkan buku baru
        penulisBookContainer.getChildren().clear();

        if (bukuSamaPenulis != null && !bukuSamaPenulis.isEmpty()) {
            int column = 0;
            int row = 1;

            // Menambahkan buku-buku dengan penulis yang sama
            for (Buku buku : bukuSamaPenulis) {
                // Periksa apakah judul buku sudah ditampilkan sebelumnya
                if (!daftarPenulisDitampilkan.contains(buku.judul)) {
                    // Menggunakan komponen FXML atau JavaFX untuk menampilkan informasi buku
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("book.fxml"));
                    try {
                        VBox bookBox = fxmlLoader.load();
                        BookController bookController = fxmlLoader.getController();
                        bookController.setData(buku);

                        // Menambahkan buku ke gridPane
                        penulisBookContainer.add(bookBox, column++, row);

                        GridPane.setMargin(bookBox, new Insets(10));

                        // Menyimpan judul buku yang sudah ditampilkan
                        daftarPenulisDitampilkan.add(buku.judul);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Handle error jika terjadi kesalahan dalam memuat komponen buku
                    }
                }
            }
        } else {
            // Menambahkan label untuk menunjukkan bahwa tidak ada buku dengan penulis yang sama
            Label label = new Label("Tidak Ada Buku dengan Penulis yang Sama");
            penulisBookContainer.add(label, 0, 0);
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
