package com.project.bukuproject;

import com.project.bukuproject.Model.Buku;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Controller untuk tampilan utama.
 */
public class HelloController implements Initializable {


    @FXML
    private GridPane bookContainer;

    @FXML
    private Button graf;

    private List<Buku> recommended;

    @FXML
    private TextField txtSearchBar;

    @FXML
    private Label header;

    @FXML
    private GridPane genreBookContainer;
    @FXML
    private GridPane penulisBookContainer;

    @FXML
    private GridPane sinopsisBookContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recommended = new ArrayList<>(books());
        loadBookContainer();
        setupSearchFunctionality();

        // Add an event listener to the txtSearchBar
        txtSearchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSearch();
            }
        });
    }

    private void setupSearchFunctionality() {
        // Add an event listener to the txtSearchBar
        txtSearchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSearch();
            }
        });
    }

    @FXML
    private void graf(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("graph.fxml"));
            Parent root = loader.load();

            // Menampilkan scene baru dengan graf
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Visualisasi Graf");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Lakukan penanganan kesalahan yang sesuai, seperti menampilkan pesan kesalahan kepada pengguna
        }
    }

    public Graph<String, String> createGraphFromBooks(List<Buku> books) {
        Graph<String, String> g = new SparseMultigraph<>();

        // Map to store vertex IDs based on genre
        Map<String, List<String>> genreVertices = new HashMap<>();
        Map<String, List<String>> authorVertices = new HashMap<>();

        // Loop through books and add vertices
        for (Buku buku : books) {
            g.addVertex(buku.judul);

            // Add vertex ID to the corresponding genre list
            for (String genre : buku.genre) {
                genreVertices.computeIfAbsent(genre, k -> new ArrayList<>()).add(buku.judul);
            }

            String penulis = buku.penulis; // Assuming 'author' is the attribute representing the author
            authorVertices.computeIfAbsent(penulis, k -> new ArrayList<>()).add(buku.judul);
        }

        // Connect vertices with the same genre
        for (List<String> genreVertexList : genreVertices.values()) {
            for (int i = 0; i < genreVertexList.size() - 1; i++) {
                for (int j = i + 1; j < genreVertexList.size(); j++) {
                    g.addEdge("Edge" + genreVertexList.get(i) + "_" + genreVertexList.get(j),
                            genreVertexList.get(i), genreVertexList.get(j));
                }
            }
        }

        for (List<String> authorVertexList : authorVertices.values()) {
            for (int i = 0; i < authorVertexList.size() - 1; i++) {
                for (int j = i + 1; j < authorVertexList.size(); j++) {
                    g.addEdge("Edge" + authorVertexList.get(i) + "_" + authorVertexList.get(j),
                            authorVertexList.get(i), authorVertexList.get(j));
                }
            }
        }

        return g;
    }








    // Memuat tata letak buku ke dalam bookContainer.

    private void loadBookContainer() {
        int column = 0;
        int row = 1;

        try {
            for (Buku buku : recommended) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("book.fxml"));
                VBox bookBox = fxmlLoader.load();
                BookController bookController = fxmlLoader.getController();
                bookController.setData(buku);

                if (column == 6) {
                    column = 0;
                    ++row;
                }

                bookContainer.add(bookBox, column++, row);
                GridPane.setMargin(bookBox, new Insets(10));
            }
        } catch (IOException e) {
            handleLoadError(e);
        }
    }

    /**
     * Menangani kesalahan saat memuat tata letak.
     *
     * @param e Exception yang ditangkap
     */
    private void handleLoadError(IOException e) {
        e.printStackTrace();
        // Lakukan penanganan kesalahan yang sesuai, seperti menampilkan pesan kesalahan kepada pengguna
    }




    /**
     * Menghasilkan daftar buku yang direkomendasikan.
     *
     * @return List of recommended books
     */
    public List<Buku> books() {
        // Implementasi logika untuk mendapatkan buku yang direkomendasikan
        List<Buku> ls = new ArrayList<>();
        Buku bukulautbercerita = new Buku("Laut Bercerita", "Leila S. Chudori", new String[] {"Drama", "Fiksi"}, "/img/img-lautbercerita.jpg",
                "Laut Bercerita, novel terbaru Leila S. Chudori, bertutur tentang kisah keluarga yang kehilangan, sekumpulan sahabat yang merasakan kekosongan di dada, sekelompok orang yang gemar menyiksa dan lancar berkhianat, sejumlah keluarga yang mencari kejelasan makam anaknya, dan tentang cinta yang tak akan luntur.");
        ls.add(bukulautbercerita);

        Buku harrypotterG = new Buku("Harry Potter and The Prisoner of Azkaban – Gryffindor", "J. K. Rowling", new String[] {"Fantasi", "Petualangan"}, "/img/img-harrypotterG.jpg",
                "Dalam edisi spesial \"Harry Potter and The Prisoner of Azkaban – Gryffindor,\" para pembaca diajak kembali merasakan keajaiban dunia sihir dengan karakter-karakter mulia dari Rumah Gryffindor di Hogwarts. Perayaan ulang tahun ke-20 dari publikasi pertama membawa kembali tahun ketiga Harry di Hogwarts yang penuh momen-momen mendebarkan, dengan tampilan warna khas rumah Gryffindor dan karya seni sampul yang memukau. Pengantar menarik mengungkap sejarah rumah ini, sementara wawasan eksklusif tentang Mantra Patronus dan gambar-gambar spektakuler oleh Levi Pinfold menambah daya tarik. Edisi ini tidak hanya memberikan pengalaman membaca yang mengesankan, tetapi juga dirancang untuk dikoleksi, merayakan keunikan Rumah Gryffindor dan menghidupkan kembali nostalgia magis di Hogwarts bagi para penggemar setia. Bagi mereka yang bermimpi menjadi Gryffindor, buku ini adalah must-have, membawa kembali kenangan duduk di bawah Topi Seleksi dengan harapan mendengar kata-kata ajaib, \"Lebih baik menjadi Gryffindor!\"");
        ls.add(harrypotterG);

        Buku harrypotterS1 = new Buku("Harry Potter and The Prisoner of Azkaban – Slytherin", "J. K. Rowling", new String[] {"Fantasi", "Petualangan"}, "/img/img-harrypotterS1.jpg",
                "Edisi Rumah Slytherin dari \"Harry Potter and The Prisoner of Azkaban\" merayakan ulang tahun ke-20 dengan kebanggaan, ambisi, dan kelicikan karakter Slytherin di Hogwarts, menggambarkan momen-momen mendebarkan tahun ketiga Harry, termasuk upaya licik Draco Malfoy untuk menyabotase Gryffindor. Dengan tampilan warna Slytherin yang khas, karya seni sampul yang indah, pengantar yang menarik, dan wawasan eksklusif, buku ini mempersembahkan gambar spektakuler oleh Levi Pinfold dari Severus Snape dengan patronusnya. Sebagai bagian dari House Editions, buku ini dirancang untuk dikoleksi, merayakan keunikan Rumah Slytherin, dan menjadi must-have bagi penggemar yang bermimpi menjadi anggota Slytherin di Hogwarts.");
        ls.add(harrypotterS1);

        Buku harrypotterS2 = new Buku("Harry Potter And The Order Of The Phoenix - Slytherin", "J. K. Rowling", new String[] {"Fantasi", "Petualangan"}, "/img/img-harrypotterS2.jpg",
                "Edisi Rumah Slytherin dari \"Harry Potter and The Order of the Phoenix\" merayakan ulang tahun ke-20, menggambarkan karakter Slytherin dengan kebanggaan, ambisi, dan kelicikan di Hogwarts. Dengan tampilan warna khas Slytherin, karya seni sampul yang indah, serta pengantar dan wawasan eksklusif, buku ini menampilkan gambar spektakuler oleh Levi Pinfold dan dirancang khusus untuk dikoleksi, merayakan keunikan Rumah Slytherin di Hogwarts.");
        ls.add(harrypotterS2);

        Buku bukucelts = new Buku("Celts: Sejarah dan Warisan Salah Satu Kebudayaan Tertua di Eropa", "Martin J. Dougherty", new String[] {"Sejarah"}, "/img/img-celtssejarahdanwarisansalahsatukebudayaantertuadieropa.jpg",
                "Sejak lama, orang-orang Celt menjadi subjek yang mendapatkan ketertarikan luar biasa, spekulasi, dan kesalahpahaman. Sebelum orang-orang Viking. Anglo-Saxons, dan Kerajaan Roma, para Celt mendominasi bagian pusat dan barat Eropa, tetapi siapakah sebenarnya orang-orang Celts ini, dan bagaimana persepsi kita tentang mereka terus berubah sepanjang sejarah");
        ls.add(bukucelts);

        Buku bukusejarahhukum = new Buku("Sejarah Hukum", "Abintoro Prakoso", new String[] {"Sejarah"}, "/img/img-sejarahhukum.jpg",
                "Suatu sistem hukum hadir sebagai alat untuk mengatur kesinambungan hidup antar masyarakat. Hukum ada untuk melindungi masyarakat sekaligus negara itu sendiri. Tanpa kehadiran hukum, keharmonisan tidak akan pernah tercipta di muka bumi, karena tidak ada suatu hal yang mengatur manusia dalam bertindak di luar normanya. Seiring dengan berkembangnya zaman, perkembangan berbagai sistem hukum pun turut berubah");
        ls.add(bukusejarahhukum);

        Buku bukuthinkandgrowrich = new Buku("Think and Grow Rich", "Napoleon Hill", new String[] {"Pengembangan Diri"}, "/img/img-thinkandgrowrich.jpg",
                "Buku ini ditulis oleh Napoleon Hill (lahir di Pound, Virginia, 26 Oktober 1883 – 8 November 1970) adalah seorang penulis Amerika Serikat. Ia dianggap luas sebagai salah satu penulis buku bertopik kesuksesan terhebat. Bukunya yang paling terkenal,Think and Grow Rich (1937), adalah salah satu buku terlaris sepanjang masa.");
        ls.add(bukuthinkandgrowrich);

        Buku bukufactfulness = new Buku("Factfulness", "Hans Rosling", new String[] {"Pengembangan Diri"}, "/img/img-factfulness.jpg",
                "INSTANT NEW YORK TIMES BESTSELLER \"One of the most important books I've ever read--an indispensable guide to thinking clearly about the world.\" - Bill Gates \"Hans Rosling tells the story of 'the secret silent miracle of human progress' as only he can. But Factfulness does much more than that. It also explains why progress is so often secret and silent and teaches readers how to see it clearly.\" ");
        ls.add(bukufactfulness);

        Buku bukuatomichabits = new Buku("Atomic Habits", "James Clear", new String[] {"Pengembangan Diri"}, "/img/img-atomichabits.jpg",
                "Kebiasaan buruk berulang lagi dan lagi bukan karena Anda tidak ingin berubah, tetapi karena Anda memiliki sistem perubahan yang salah. Anda tidak naik ke tingkat tujuan Anda. Anda jatuh ke tingkat sistem Anda. Di sini, Anda akan mendapatkan sistem terbukti yang dapat membawa Anda ke tingkat yang lebih tinggi.");
        ls.add(bukuatomichabits);

        Buku bukuiloveyouayah = new Buku("I Love You, Ayah", "Soekanto Sa", new String[] {"Komik"}, "/img/img-iloveyouayah.jpg",
                "Ayah bukan sosok sempurna. Ia tidak terlalu tampan dan tidak juga jelek. Ayah akan melindungiku bila ada yang menggangguku. Bagaimana dengan ayahmu?");
        ls.add(bukuiloveyouayah);

        Buku bukukitabfirasat = new Buku("Kitab Firasat", " Imam Fakhruddin Ar-Razi", new String[] {"Filsafat"}, "/img/img-kitabfirasat.jpg",
                "Buku di tangan Anda ini adalah bukti yang bisa dibilang melampaui zamannya jauh sebelum para ilmuwan modern di bidang Fisiognomi (Ilmu membaca manusia dari bentuk fisiknya), seperti Gerolamo Cardano (1501-1576 M) dan Giambattista Della Porta (1535-1615 M) menulis buku-buku Fisiognomi. Penulis buku ini (Imam Fakhruddin ar-Razi) sudah terlebih dahulu mengenalkan ini kepada dunia.");
        ls.add(bukukitabfirasat);

        Buku bukumanaqibimamasysyafi = new Buku("Manaqib Imam Asy-Syafi`I", " Imam Fakhruddin Ar-Razi", new String[] {"Filsafat"}, "/img/img-kitabfirasat.jpg",
                "Di Indonesia, Imam Asy-Syafi’I merupakan ulama besar yang sudah tidak asing lagi, sebab pandangan-pandangan mazhab beliau mewarnai cara berislam mayoritas muslim Indonesia. Sayangnya, tidak sedikit di antara kita yang baru mengenal Imam Asy-Syafi’I sebatas namanya saja..");
        ls.add(bukumanaqibimamasysyafi);

        Buku bukuandalusiasejarahlengkapdariawalpenaklukanmuslim = new Buku("Andalusia: Sejarah Lengkap dari Awal Penaklukan Muslim", " abdurahman ali", new String[] {"Sejarah"}, "/img/img-andalusiasejarahlengkapdariawalpenaklukanmuslim.jpg",
                "Al-Andalus adalah nama dari bagian Semenanjung Iberia (Spanyol dan Portugal) yang diperintah oleh orang Islam, atau orang Moor antara tahun 711 dan 1492. Al-Andalus juga sering disebut Andalusia, tetapi penggunaan ini memiliki keambiguan dengan wilayah administratif di Spanyol modern Andalusia.");
        ls.add(bukuandalusiasejarahlengkapdariawalpenaklukanmuslim);

        Buku bukusirahnabawiyah = new Buku("Sirah Nabawiyah: Sejarah Lengkap Kehidupan Nabi Muhammad", " Syekh Shafiyurrahman", new String[] {"Sejarah", "Islam"}, "/img/img-sirahnabawiyah.jpg",
                "Sirah Rasulullah S.A.W. memang tak pernah kering untuk digali dan tak pernah habis untuk ditulis. Para ulama dan sejarawan sudah banyak yang menjelaskan dan meriwayatkan segenap aspek kehidupan beliau serta setiap peristiwa dan kejadian yang beliau alami");
        ls.add(bukusirahnabawiyah);

        Buku funiculifunicula1 = new Buku("Funiculi Funicula (Kōhī Ga Samenai Uchi Ni---Before\n the Coffee Gets Cold)", "Toshikazu Kawaguchi", new String[] {"Time Traveler", "Fiksi"}, "/img/img-funiculifunicula1.jpg",
                "Kafe tua yang berada di gang kecil Tokyo terletak di bawah gedung lain, tidak butuh pendingin untuk mendinginkan Kafe tersebut. Tidak begitu ramai, namun terkenal karena bisa membawa pengunjungnya menjelajahi waktu. Keajaiban kafe itu menarik seorang wanita yang ingin memutar waktu untuk berbaikan dengan kekasihnya, seorang perawat yang ingin membaca surat yang tak sempat diberikan suaminya yang sakit, seorang kakak yang ingin menemui adiknya untuk terakhir kali, dan seorang ibu yang ingin bertemu dengan anak yang mungkin takkan pernah dikenalnya. Namun ada banyak peraturan yang harus diingat. Satu, mereka harus tetap duduk di kursi yang telah ditentukan. Dua, apapun yang mereka lakukan di masa yang didatangi takkan mengubah kenyataan di masa kini. Tiga, mereka harus menghabiskan kopi khusus yang disajikan sebelum kopi itu dingin. Rentetan peraturan lainnya tak menghentikan orang-orang itu untuk menjelajahi waktu. Akan tetapi, jika kepergian mereka tak mengubah satu hal pun di masa kini, layakkah semua itu dijalani?");
        ls.add(funiculifunicula1);

        Buku funiculifunicula2 = new Buku("Funiculi Funicula 2: Kisah-Kisah\n Yang Baru Terungkap", "Toshikazu Kawaguchi", new String[] {"Time Traveler", "Fiksi"}, "/img/img-funiculifunicula2.jpg",
                "Funiculi Funicula, sebuah kafe di gang sempit di Tokyo, masih kerap didatangi oleh orang-orang yang ingin menjelajah waktu. Peraturan-peraturan yang merepotkan masih berlaku, tetapi itu semua tidak menyurutkan harapan mereka untuk memutar waktu. Kali ini ada seorang pria yang ingin kembali ke masa lalu untuk menemui sahabat yang putrinya ia besarkan, seorang putra putus asa yang tidak menghadiri pemakaman ibunya, seorang pria sekarat yang ingin melompat kedua tahun kemudian untuk memastikan kekasihnya bahagia, dan seorang detektif yang ingin memberi istrinya hadiah ulang tahun untuk pertama sekaligus terakhir kalinya. Kenyataan memang akan tetap sama. Namun dalam singkatnya durasi sampai kopi mendingin, mungkin masih tersisa waktu bagi mereka untuk menghapus penyesalan, membebaskan diri dari rasa bersalah atau mungkin melihat terwujudnya harapan.");
        ls.add(funiculifunicula2);

        Buku bukuchasingunicorn = new Buku("Chasing Unicorns", "Nicko Widjaja", new String[] {"Bisnis", "Ekonomi"}, "/img/img-chasingunicorns.jpg",
                "Chasing Unicorns: In Search of Fool’s Gold is a candid exploration of the intricate venture capital landscape in Indonesia and Southeast Asia. It’s an insider’s perspective that offers a unique and comprehensive understanding of the ecosystem’s volatile nature. Nicko Widjaja, a seasoned professional in the startup investment realm, shares firsthand experiences, insights, and revelations about the challenges and opportunities that define this dynamic sector.");
        ls.add(bukuchasingunicorn);

        Buku bukuhowinnovationworks = new Buku("How Innovation Works", "Matt Ridley", new String[] {"Bisnis", "Ekonomi"}, "/img/img-howinnovationworks.jpg",
                "Building on his national bestseller The Rational Optimist, Matt Ridley chronicles the history of innovation, and how we need to change our thinking on the subject. Innovation is the main event of the modern age, the reason we experience both dramatic improvements in our living standards and unsettling changes in our society. Forget short-term symptoms like Donald Trump and Brexit, it is innovation itself that explains them and that will itself shape the 21st century for good and ill. Yet innovation remains a mysterious process, poorly understood by policy makers and businessmen, hard to summon into existence to order, yet inevitable and inexorable when it does happen. Matt Ridley argues in this book that we need to change the way we think about innovation, to see it as an incremental, bottom-up, fortuitous process that happens to society as a direct result of the human habit of exchange, rather than an orderly, top-down process developing according to a plan. Innovation is crucially different from invention, because it is the turning of inventions into things of practical and affordable use to people. It speeds up in some sectors and slows down in others. It is always a collective, collaborative phenomenon, not a matter of lonely genius. It is gradual, serendipitous, recombinant, inexorable, contagious, experimental and unpredictable. It happens mainly in just a few parts of the world at any one time. It still cannot be modelled properly by economists, but it can easily be discouraged by politicians. Far from there being too much innovation, we may be on the brink of an innovation famine. Ridley derives these and other lessons, not with abstract argument, but from telling the lively stories of scores of innovations, how they started and why they succeeded or in some cases failed. He goes back millions of years and leaps forward into the near future. Some of the innovation stories he tells are about steam engines, jet engines, search engines, airships, coffee, potatoes, vaping, vaccines, cuisine, antibiotics, mosquito nets, turbines, propellers, fertiliser, zero, computers, dogs, farming, fire, genetic engineering, gene editing, container shipping, railways, cars, safety rules, wheeled suitcases, mobile phones, corrugated iron, powered flight, chlorinated water, toilets, vacuum cleaners, shale gas, the telegraph, radio, social media, block chain, the sharing economy, artificial intelligence, fake bomb detectors, phantom games consoles, fraudulent blood tests, faddish diets, hyperloop tubes, herbicides, copyright and even―a biological innovation―life itself.");
        ls.add(bukuhowinnovationworks);

        Buku bukusecretsofthemillionairemind = new Buku("Secrets of the Millionaire Mind", "T Harv Eker", new String[] {"Bisnis", "Ekonomi"}, "/img/img-secretsofthemillionairemind.jpg",
                "Pernahkah Anda bertanya-tanya mengapa beberapa orang tampaknya menjadi kaya dengan mudah, sementara yang lain ditakdirkan untuk hidup dalam kesulitan keuangan? Apakah perbedaan ditemukan dalam pendidikan, kecerdasan, keterampilan, waktu, kebiasaan kerja, kontak, keberuntungan, atau pilihan pekerjaan, bisnis, atau investasi mereka?\n" +
                        "\n" +
                        "Dalam terobosannya Secrets of the Millionaire Mind, T. Harv Eker menyatakan: \"Beri saya lima menit, dan saya dapat memprediksi masa depan finansial Anda selama sisa hidup Anda!\" Eker melakukannya dengan mengidentifikasi \"cetak biru uang dan kesuksesan\" Anda. Kita semua memiliki cetak biru uang pribadi yang tertanam dalam pikiran bawah sadar kita, dan cetak biru inilah, lebih dari segalanya, yang akan menentukan kehidupan finansial kita");
        ls.add(bukusecretsofthemillionairemind);

        Buku bukuartikulasirasamencintaikecantikandirisepenuhnya = new Buku("Artikulasi Rasa: Mencintai Kecantikan Diri Sepenuhnya", "Selvia Liem", new String[] {"Pengembangan Diri"}, "/img/img-artikulasirasamencintaikecantikandirisepenuhnya.jpg",
                "“Sebenarnya kamu cantik sih, tapi kurang kurus aja,” “Coba deh, kamu perawatan sedikit biar lebih cerah kulitnya,” “Sepertinya kalau rambutmu dibuat lurus akan lebih cantik, ya,” Dan masih banyak lagi komentar dan tuntutan sosial yang mengharuskan kita tampil “cantik” berdasarkan standar kecantikan yang ada. Kamu pernah mengalaminya? \nPadahal standar kecantikan itu hanyalah standar yang dibentuk oleh media massa, oleh media sosial. Sampai-sampai semua sudut dan bagian tubuh kita harus sama persis dengan standar tersebut, barulah kita bisa dibilang cantik. Hmmm… agak sedih ya lihatnya. Padahal kita sebagai perempuan tidak harus mengikuti standar kecantikan yang dibentuk oleh media tersebut. \nPadahal kita sebagai perempuan punya ciri khas dan keunikan masing-masing. Kita hanya perlu mencintai kecantikan diri kita dengan sepenuhnya, menerima diri kita dengan sepenuhnya, dengan begitu kita mampu untuk bersinar. Buku ini akan mengajakmu untuk menciptakan dan mencintai kecantikan diri sendiri dengan pendekatan self-love. Apakah kamu pernah mengalaminya?");
        ls.add(bukuartikulasirasamencintaikecantikandirisepenuhnya);

        Buku bukuartofwar = new Buku("Art Of War.The: Menelusuri Strategi & Taktik Perang Ala Sun", "Andri Wang", new String[] {"Pengembangan Diri"}, "/img/img-artofwar.jpg",
                "The Art of War-Sun Zi adalah buku perang pertama di dunia. Buku yang terdiri atas 5.900 huruf klasik Tiongkok ini disajikan kembali oleh Andri Wang dengan penjelasan yang akan memudahkan pembaca dalam memahaminya. Tidak hanya dibaca oleh kalangan angkatan bersenjata, buku ini juga menjadi pedoman bagi para pelaku bisnis dalam menjawab persaingan masa kini yang begitu ketat. Strategi dan taktik perang yang relevan untuk persaingan sepanjang masa ini akan membantu kita dalam memenangkan segala kompetisi.");
        ls.add(bukuartofwar);

        Buku bukusamkok = new Buku("Sam Kok: Perang Siasat VS. Siasat Tiga Kerajaan", "Andri Wang", new String[] {"Pengembangan Diri"}, "/img/img-samkok.jpg",
                "Buku asli Roman Tiga Kerajaan (EX) atau lebih dikenal dengan Sam Kok (EB) ini ditulis dalam bahasa klasik Tiongkok Kuno yang terdiri atas 120 bab dan 600.000 lebih kata-kata yang tidak mudah dibaca habis sekaligus. Tak dapat dimungkiri, buku yang sangat tebal ini telah menjadi salah satu novel paling termasyhur dan paling penting dalam literatur klasik Tiongkok dan diterjemahkan ke berbagai bahasa dengan berbagai variasinya novel yang ditulis Luo Guan Zhong (3) ini mengisahkan peperangan tiga negara Wei (), Shu (5), dan Wu (). Selain mempunyai nilai sastra dan sejarah yang tinggi, novel ini juga sarat dengan ilmu strategi serta siasat yang mengagumkan dan tak lekang waktu. Dari ratusan peperangan besar atau kecil itulah kita bisa belajar berbagai taktik dan strategi untuk kita gunakan dalam membangun dan mengembangkan bisnis maupun kehidupan kita. Karakter dan kepribadian beberapa tokoh heroik penting pada waktu itu seperti Liu Bei, Guan Yu, Zhang Fei, Zhu Ge Liang, Zhao Yun, Cao Cao, Zhuo Yu, Sun Quan, Lu Xiao, dan tokoh-tokoh lainnya dilukiskan secara hidup dan mengesankan. Banyak orang ingin membacanya, namun urung karena ketebalan buku tersebut. Untuk menghemat waktu pembaca, buku ini disusun sedemikian rupa, dipilih.cerita-cerita yang representatif dari buku asli Sam Kok, lalu diekstraksi menjadi 24 Bab sebagai \"bird eye view kisah Sam Kok. Anda tidak hanya mendapatkan gambaran besar, tapi juga belajar berbagai siasat dan strategi yang akan membuat Anda lebih hati-hati, lebih melihat sesuatu dari berbagai perspektif sehingga tidak salah melangkah, dan lebih bijak.");
        ls.add(bukusamkok);

        Buku bukuberpikirdiluarpemikiranorangawam = new Buku("Berpikir Di Luar Pemikiran Orang Awam", "Andri Wang", new String[] {"Pengembangan Diri"}, "/img/img-berpikiriluarpemikiranorangawam.jpg",
                "Manusia yang keras kepala cenderung mempertahankan keyakinannya terkait mana yang benar dan salah. Namun, Zhuang Zi mengatakan bahwa penilaian itu relatif dan tidak mutlak. Orang awam menganggap bahwa harta yang melimpah dan posisi yang terpandang akan membuatnya dihargai orang. Namun, bagi Zhuang Zi, dia lebih suka menjadi orang biasa-biasa saja dan tidak dikenal orang. Ia tidak mau peduli dengan untung dan rugi atau segala jabatan di dunia ini. Orang awam selalu mempunyai nafsu keinginan, tapi Zhuang Zi berkata bahwa kita seharusnya mengurangi ego dan hanya memiliki sedikit keinginan. Orang awam suka bicara, namun falsafah Dao menganggap banyak bicara banyak salah, lebih baik diam saja dan tidak usah bicara. Ada banyak peristiwa yang tidak bisa diubah sesuai kehendak manusia. Zhuang Zi selalu menerima apa adanya semua yang tidak bisa diubah sesuai kehendaknya. Ia tidak ingin intervensi dan memaksakan kehendak, juga tidak ingin bersaing dengan siapa pun. Perilakunya wu wei.");
        ls.add(bukuberpikirdiluarpemikiranorangawam);

        Buku bukupanduanjitufisikasma= new Buku("Panduan Jitu Fisika SMA", "Sony Adam Saputra, S.Si.", new String[] {"Sains"}, "/img/img-panduanjitufisikasma.jpg",
                "Belajar fisika enggak akan seru tanpa menghafal dan menghafal tidak akan lengkap tanpa sebuah buku yang isinya merangkum semua hal. Sebagian orang tidak mau mempelajari fisika karena memang materinya BANYAK, istilah-istilahnya MEMBINGUNGKAN,dan TIDAK PRAKTIS seperti ilmu alam yang lain. Buku Ini disusun sedemikian rupa dengan memperhatikan segala aspek dan perpaduan pasa kurikulum yang berlaku saat ini. Fisika seringkali melibatkan konsep matematika yang kuat. Pastikan Anda memahami dasar-dasar seperti aljabar, trigonometri, dan kalkulus. Mulailah dengan memahami konsep dasar fisika seperti gerak, gaya, energi, listrik, dan magnetisme. Pastikan Anda memiliki dasar yang kuat sebelum melangkah ke konsep yang lebih kompleks.");
        ls.add(bukupanduanjitufisikasma);

        Buku bukupanduanjitumatematikasma= new Buku("Panduan Jitu Matematika SMA", "Sony Adam Saputra, S.Si.", new String[] {"Sains"}, "/img/img-panduanjitumatematikasma.jpg",
                "Belajar matematika nggak akan seru tanpa menghafal dan menghafal tidak akan lengkap tanpa sebuah buku yang isinya merangkum semua hal. Sebagian orang tidak mau mempelajari matematika karena memang materinya BANYAK, istilah-istilah yg MEMBINGUNGKAN, dan TIDAK PRAKTIS seperti ilmu alam yang lain" );
        ls.add(bukupanduanjitumatematikasma);

        Buku bukupanduanjitukimiasma= new Buku("Panduan Jitu Kimia SMA", "Sony Adam Saputra, S.Si.", new String[] {"Sains"}, "/img/img-panduanjitukimiasma.jpg",
                "Buku ini adalah rangkuman pelajaran Kimia SMA dari kelas X hingga kelas XII yang sangat dibutuhkan para pelajar untuk mempersiapkan diri menyongsong ujian tingkat sekolah, nasional atau ujian masuk ke perguruan tinggi. Buku ini penting bagi siswa-siswi SMA yang akan menempuh ujian akhir sekolah dan ujian masuk ke perguruan tinggi sebagai bahan yang bisa dipelajari kapan pun dimana pun karena bentuknya yang handy dan pocket-size.\nSo, tunggu apa lagi, dalam semalam setelah membaca dan memahami buku ini, kamu bisa langsung jadi AHLI KIMIA dan langsung bisa membabat habis semua soal kimia yang muncul di ujian-ujian sekolah kamu dengan mudah. Semoga, kamu juga langsung bisa jadi RANGKING SATU!");
        ls.add(bukupanduanjitukimiasma);



        return ls;
    }




    @FXML
    private void handleSearch() {
        String searchTerm = txtSearchBar.getText().trim().toLowerCase();
        List<Buku> searchResults = searchBooks(searchTerm);

        // Clear existing content in the bookContainer
        bookContainer.getChildren().clear();


        // Load the search results into the bookContainer
        loadSearchResults(searchResults);
    }

    private List<Buku> searchBooks(String searchTerm) {
        List<Buku> results = new ArrayList<>();
        for (Buku buku : recommended) {
            // Pencarian berdasarkan judul, yang setidaknya satu kata cocok
            if (containsAllWordsIgnoreCase(buku.getJudul(), searchTerm)) {
                results.add(buku);
            }
            // Pencarian berdasarkan penulis, yang setidaknya satu kata cocok
            else if (containsAllWordsIgnoreCase(buku.getPenulis(), searchTerm)) {
                results.add(buku);
            }
            // Pencarian berdasarkan genre, yang setidaknya satu kata cocok
            else if (containsAllWordsIgnoreCase(Arrays.toString(buku.getGenre()), searchTerm)) {
                results.add(buku);
            }
        }
        return results;
    }

    private static boolean containsAllWordsIgnoreCase(String text, String search) {

        String[] searchWords = search.split("\\s+");

        StringBuilder combinedSearchBuilder = new StringBuilder();
        for (String searchWord : searchWords) {
            combinedSearchBuilder.append(searchWord.replaceAll("[^a-zA-Z0-9]", ""));
        }
        String combinedSearch = combinedSearchBuilder.toString().toLowerCase();


        String[] words = text.split("\\s+");

        StringBuilder combinedTextBuilder = new StringBuilder();
        for (String word : words) {
            combinedTextBuilder.append(word.replaceAll("[^a-zA-Z0-9]", ""));
        }
        String combinedText = combinedTextBuilder.toString().toLowerCase();

        // Mengecek apakah kata-kata gabungan ditemukan dalam teks gabungan
        return combinedText.contains(combinedSearch);
}


    private void loadSearchResults(List<Buku> searchResults) {
        int column = 0;
        int row = 1;

        try {
            if (searchResults.isEmpty()) {
                header.setText("Maaf, kami tidak menemukan apa yang Anda cari");
            } else {
                StringBuilder headerTextBuilder = new StringBuilder("Hasil pencarian: ");
                header.setText(headerTextBuilder.toString());
            }

            for (Buku buku : searchResults) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("book.fxml"));
                VBox bookBox = fxmlLoader.load();
                BookController bookController = fxmlLoader.getController();
                bookController.setData(buku);

                if (column == 6) {
                    column = 0;
                    ++row;
                }

                bookContainer.add(bookBox, column++, row);
                GridPane.setMargin(bookBox, new Insets(10));
            }
        } catch (IOException e) {
            handleLoadError(e);
        }
    }
}
