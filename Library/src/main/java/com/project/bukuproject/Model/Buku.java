package com.project.bukuproject.Model;

import java.util.*;

public class Buku {
    public String judul;
    public String penulis;
    public String[] genre;
    public String ImgSrc;
    public String sinopsis;
    public List<Buku> bukuTerhubungGenre;
    public List<Buku> bukuTerhubungPenulis;
    public List<Buku> bukuTerhubungRekomendasi;
    public static List<Buku> daftarBuku = new ArrayList<>();
    public boolean sudahDitampilkan;
    public String[] simpanSinopsis;

    public String getJudul() {
        return judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public Buku(String judul, String penulis, String[] genre, String ImgSrc) {
        this.judul = judul;
        this.penulis = penulis;
        this.genre = genre;
        this.ImgSrc = ImgSrc;
        daftarBuku.add(this);
        this.sudahDitampilkan = false;
    }

    public Buku(String judul, String penulis, String[] genre, String ImgSrc, String sinopsis) {
        this.judul = judul;
        this.penulis = penulis;
        this.genre = genre;
        this.ImgSrc = ImgSrc;
        this.sinopsis = sinopsis.replaceAll("[^a-zA-Z0-9 ]", "");

        this.simpanSinopsis = this.sinopsis.split(" ");
        daftarBuku.add(this);
        this.sudahDitampilkan = false;
    }


    // Awal Cek Genre
    public void hubungkanSimpulGenre(Buku bukuTujuan) {
        if (bukuTerhubungGenre == null) {
            bukuTerhubungGenre = new ArrayList<>();
        }
        bukuTerhubungGenre.add(bukuTujuan);
    }

    public void cekGenreDFS(List<Buku> daftarBuku) {
        if (bukuTerhubungGenre == null && !sudahDitampilkan) {
            Stack<Buku> stack = new Stack<>();
            stack.push(this);

            while (!stack.isEmpty()) {
                Buku bukuIni = stack.pop();

                if (!bukuIni.sudahDitampilkan) {
                    bukuIni.sudahDitampilkan = true;
                }

                for (Buku bukuLain : daftarBuku) {
                    if (!bukuLain.sudahDitampilkan && !bukuLain.equals(bukuIni) && bukuIni.genreSamaDenganBukuLain(bukuLain)) {
                        bukuIni.hubungkanSimpulGenre(bukuLain);
                        stack.push(bukuLain);
                    }
                }
            }

            for (Buku buku : daftarBuku) {
                buku.sudahDitampilkan = false;
            }
            tampilkanBukuSamaGenre();
        }
    }



    public void cekGenre(List<Buku> daftarBuku) {
        if (bukuTerhubungGenre == null) {
            for (Buku bukuLain : daftarBuku) {
                if (!bukuLain.equals(this) && genreSamaDenganBukuLain(bukuLain)) {
                    hubungkanSimpulGenre(bukuLain);
                }
            }
            tampilkanBukuSamaGenre();
        }
    }

    public void tampilkanBukuSamaGenre() {
        if (bukuTerhubungGenre != null && !bukuTerhubungGenre.isEmpty()) {
            System.out.print("Buku dengan genre yang sama dengan buku " + this.judul + ": ");
            for (Buku buku : bukuTerhubungGenre) {
                System.out.print(buku.judul + ", ");
            }
            System.out.println();
        } else {
            System.out.println("Tidak ada buku dengan genre yang sama dengan " + this.judul);
        }
    }

    public boolean genreSamaDenganBukuLain(Buku bukuLain) {
        if (bukuLain != null && this.genre != null && bukuLain.genre != null) {
            for (String genreLain : bukuLain.genre) {
                for (String genreIni : this.genre) {
                    if (genreIni.equals(genreLain)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    // Akhir Cek Genre

    // Awal Cek Penulis
    public void hubungkanSimpulPenulis(Buku bukuTujuan) {
        if (bukuTerhubungPenulis == null) {
            bukuTerhubungPenulis = new ArrayList<>();
        }
        bukuTerhubungPenulis.add(bukuTujuan);
    }

    public void cekPenulis(List<Buku> daftarBuku) {
        if (bukuTerhubungPenulis == null) {
            for (Buku bukuLain : daftarBuku) {
                if (!bukuLain.equals(this) && penulisSamaDenganBukuLain(bukuLain)) {
                    hubungkanSimpulPenulis(bukuLain);
                }
            }
            tampilkanBukuSamaPenulis();
        }
    }

    public void tampilkanBukuSamaPenulis() {
        if (bukuTerhubungPenulis != null && !bukuTerhubungPenulis.isEmpty()) {
            System.out.print("Buku dengan penulis yang sama dengan buku " + this.judul + ": ");
            for (Buku buku : bukuTerhubungPenulis) {
                System.out.print(buku.judul + ", ");
            }
            System.out.println();
        } else {
            System.out.println("Tidak ada buku dengan penulis yang sama dengan " + this.judul);
        }
    }

    public boolean penulisSamaDenganBukuLain(Buku bukuLain) {
        return bukuLain != null && this.penulis != null && this.penulis.equals(bukuLain.penulis);
    }
    // Akhir Cek Penulis


    // Awal Cek Rekomendasi
    public void rekomendasi(List<Buku> daftarBuku) {
        List<Buku> rekomendasiBuku = new ArrayList<>();

        for (Buku bukuIni : daftarBuku) {
            if (!this.equals(bukuIni)) {
                int jumlahKataSama = hitungKataSama(bukuIni);

                // Check bidirectional recommendation
                if (jumlahKataSama > 20) {
                    rekomendasiBuku.add(bukuIni);

                    // Tambahkan buku rekomendasi ke dalam list bukuTerhubungRekomendasi
                    if (bukuIni.bukuTerhubungRekomendasi == null) {
                        bukuIni.bukuTerhubungRekomendasi = new ArrayList<>();
                    }
                    bukuIni.bukuTerhubungRekomendasi.add(this);
                }
            }
        }

        // Tampilkan hasil rekomendasi
        if (!rekomendasiBuku.isEmpty()) {
            System.out.print("Buku yang harus Anda baca sebelum atau setelah buku " + this.judul + ": ");
            for (Buku buku : rekomendasiBuku) {
                System.out.print(buku.judul);
                if (rekomendasiBuku.indexOf(buku) < rekomendasiBuku.size() - 1) {
                    System.out.print(", ");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        } else {
            System.out.println("Tidak ada rekomendasi buku untuk " + this.judul);
        }

    }

    public int hitungKataSama(Buku bukuLain) {
        String[] kataListIni = this.simpanSinopsis;
        String[] kataListLain = bukuLain.simpanSinopsis;

        // Check for null arrays
        if (kataListIni == null || kataListLain == null) {
            return 0;
        }

        List<String> kataListLainList = List.of(kataListLain);
        List<String> kataSudahDicek = new ArrayList<>();

        int jumlahKataSama = 0;

        // Menghitung kata yang sama
        for (String kata : kataListIni) {
            if (kataListLainList.contains(kata) && !kataSudahDicek.contains(kata)) {
                jumlahKataSama++;
                kataSudahDicek.add(kata);
            }
        }
        return jumlahKataSama;
    }

    public String[] getGenre() {
        return genre;
    }

    // Akhir Cek Rekomendasi
}