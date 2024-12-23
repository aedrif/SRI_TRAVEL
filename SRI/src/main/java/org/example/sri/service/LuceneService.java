package org.example.sri.service;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.example.sri.models.Document;
import org.example.sri.models.Token;
import org.example.sri.repo.DocumentRepository;
import org.example.sri.repo.TokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class LuceneService {

    private final DocumentRepository documentRepository;
    private final TokenRepository tokenRepository;
    private final Directory bm25IndexDirectory;
    private final Directory tfidfIndexDirectory;
    private final EnglishAnalyzer analyzer;
    private final Set<String> importantTerms;
    private final Set<String> destinationTerms;
    private final Set<String> activitiesTerms;
    private final Set<String> accomodationTerms;




    public LuceneService(
            DocumentRepository documentRepository,
            TokenRepository tokenRepository,
            @Value("${lucene.index.bm25.path:./lucene-bm25-index}") String bm25IndexPath,
            @Value("${lucene.index.tfidf.path:./lucene-tfidf-index}") String tfidfIndexPath
    ) throws IOException {
        this.documentRepository = documentRepository;
        this.tokenRepository = tokenRepository;

        // Initialize directories
        this.bm25IndexDirectory = FSDirectory.open(Paths.get(bm25IndexPath));
        this.tfidfIndexDirectory = FSDirectory.open(Paths.get(tfidfIndexPath));
        Files.createDirectories(Paths.get(bm25IndexPath));
        Files.createDirectories(Paths.get(tfidfIndexPath));

        this.analyzer = new EnglishAnalyzer();

        // Initialize both indices
        initializeIndex(this.bm25IndexDirectory, new BM25Similarity());
        initializeIndex(this.tfidfIndexDirectory, new ClassicSimilarity());

        this.importantTerms = new HashSet<>(Arrays.asList(
                "abroad", "aborigin", "access", "adapt","winter", "snow", "admiss", "adrenalin", "adventur", "advertis", "advisor", "airfar", "airlin", "airport", "airplan", "alpin", "altitud", "amen", "amphitheat", "amus", "anchor", "ancient", "agenc", "agent", "accommod", "hotel", "hostel", "resort", "motel", "inn", "guesthous", "lodg", "homestai", "timeshar", "campground", "campsit", "caravan", "cottag", "villa", "apart", "bedandbreakfast", "suit", "penthous", "dormitori", "glamp", "bungalow", "ryokan", "chalet", "cabin", "farmhous", "yurt", "transport", "travel", "trip", "journey", "expedit", "voyag", "itinerari", "rout", "path", "roadtrip", "cruis", "ferri", "boat", "ship", "yacht", "speedboat", "submarin", "aircraft", "helicopt", "balloon", "railwai", "train", "tram", "metro", "subwai", "bus", "coach", "taxi", "cab", "rickshaw", "bicycl", "bike", "scooter", "motorcycl", "car", "rentalcar", "rideshar", "transfer", "shuttl", "port", "harbor", "termin", "station", "platform", "runwai", "corridor", "lane", "passport", "visa", "ticket", "boardingpass", "idcard", "identif", "permit", "licens", "reserv", "book", "confirm", "voucher", "insur", "polici", "coverag", "claim", "custom", "immigr", "checkpoint", "secur", "declar", "restrict", "regul", "guidelin", "embassi", "consul", "bordercontrol", "baggag", "luggag", "carryon", "suitcas", "backpack", "daypack", "duffelbag", "trunk", "cargo", "tag", "label", "carous", "sightsee", "excurs", "tour", "guidedtour", "selfguid", "adventur", "safari", "trek", "hike", "climb", "mountain", "ski", "snowboard", "surf", "windsurf", "kitesurf", "dive", "snorkel", "raft", "kayak", "canoe", "sail", "fish", "hunt", "birdwatch", "wildlifeview", "wildlife", "photographi", "film", "camp", "picnick", "stargaz", "sunbath", "swim", "bath", "spa", "massag", "well", "medit", "yoga", "festiv", "carnival", "concert", "parad", "museum", "galleri", "exhibit", "monument", "landmark", "heritag", "templ", "church", "mosqu", "synagogu", "cathedr", "palac", "castl", "fort", "fortress", "ruin", "archaeologicalsit", "restaurant", "cafe", "bar", "pub", "bistro", "cafeteri", "eateri", "streetfood", "cuisin", "gourmet", "specialtydish", "localdish", "delicaci", "tastingmenu", "buffet", "brunch", "dinner", "lunch", "breakfast", "beverag", "cocktail", "wine", "wineri", "breweri", "distilleri", "barista", "chef", "waiter", "waitress", "sommeli", "tradit", "custom", "folklor", "handicraft", "artisan", "ritual", "ceremoni", "costum", "languag", "dialect", "translat", "interpret", "guid", "guidanc", "guidebook", "glossari", "phrasebook", "signlanguag", "multilingu", "tourismboard", "touristoffic", "visitorcent", "hospital", "host", "hostess", "guest", "local", "resid", "citizen", "expat", "tourism", "tourist", "tourismsector", "touroper", "travelagenc", "travelagent", "bookingplatform", "reservationdesk", "frontdesk", "concierg", "bellboi", "porter", "housekeep", "roomservic", "laundryservic", "mainten", "manag", "recept", "receptionist", "hospitalindustri", "cater", "tourpackag", "packageholidai", "allinclus", "halfboard", "fullboard", "roomonli", "bedonli", "travelinsur", "medicalinsur", "travelbrok", "wholesal", "retail", "onlinebook", "offlinebook", "aggreg", "metasearch", "distribut", "channelmanag", "mountain", "hill", "nightlife", "valley", "plain", "plateau", "desert", "forest", "jungl", "rainforest", "savannah", "grassland", "stepp", "tundra", "glacier", "volcano", "crater", "cav", "canyon", "cliff", "gorg", "waterfal", "cascad", "river", "stream", "creek", "brook", "lake", "pond", "lagoon", "swamp", "marsh", "bog", "coastlin", "shor", "beach", "dune", "bai", "gulf", "peninsula", "island", "archipelago", "ocean", "sea", "canal", "channel", "strait", "fjord", "reef", "coralreef", "marin", "weather", "climat", "season", "monsoon", "hurrican", "typhoon", "cyclon", "storm", "rainfal", "humid", "temperatur", "sunni", "cloudi", "raini", "windi", "snowi", "foggi", "misti", "drought", "flood", "uvindex", "forecast", "warm", "hot", "cold", "mild", "cool", "freez", "travelseason", "offseason", "peakseason", "shoulderseason", "map", "compass", "gp", "navig", "routeplann", "travelapp", "smartphon", "charg", "convert", "powerbank", "flashlight", "headlamp", "binocular", "camera", "tripod", "len", "memorycard", "batteri", "earplug", "eyemask", "neckpilow", "blanket", "sleepingbag", "tent", "hammock", "sunscreen", "insectrepel", "firstaid", "medicin", "bandag", "passporthold", "wallet", "moneybelt", "waterproofbag", "raincoat", "umbrella", "sunglass", "hat", "scarf", "glove", "jacket", "coat", "footwear", "boot", "sandal", "sneaker", "flipflop", "toiletri", "toothpast", "toothbrush", "shampoo", "soap", "towel", "laundrybag", "ziplockbag", "advertis", "billboard", "brochur", "pamphlet", "flyer", "coupon", "discount", "deal", "offer", "promo", "campaign", "brand", "loyaltyprogram", "membership", "affili", "influenc", "testimoni", "review", "rate", "feedback", "referr", "ambassador", "sponsor", "partner", "collabor", "directori", "list", "inventori", "exclus", "premium", "luxuri", "upscal", "boutiquehotel", "brandhotel", "chainhotel", "independenthotel", "resortchain", "themepark", "attractionpark", "aquapark", "wildlifepark", "nationalpark", "protectedarea", "reservationcent", "travelguid", "travelblog", "travelvlog", "magazin", "newspap", "editori", "journal", "documentary", "filmfestiv", "interview", "influencertravel", "sponsortrip", "mediacoverag", "photocompetit", "writingcontest", "tourismaward", "currenc", "exchang", "transact", "pay", "cash", "creditcard", "debitcard", "bank", "atm", "travellerchequ", "commiss", "tip", "gratu", "surcharg", "fee", "budget", "lowcost", "economypackag", "exchangeoffic", "plan", "planner", "arrang", "schedul", "timetabl", "agenda", "checklist", "note", "notebook", "pen", "pencil", "highlight", "prioriti", "strategi", "research", "comparison", "choos", "select", "option", "altern", "bookingengin", "distributionchannel", "yieldmanag", "revenu", "profit", "margin", "cost", "expens", "save", "alloc", "resourc", "logist", "suppl", "demand", "predict", "analysi", "segment", "nich", "massmarket", "target", "audienc", "focusgroup", "survey", "poll", "questionnair", "insight", "data", "statist", "databas", "bigdata", "cloudservic", "backup", "upgrad", "updat", "overhaul", "renov", "refurbish", "extend", "expand", "franchis", "chain", "trademark", "author", "complianc", "standard", "legisl", "protocol", "codeofconduct", "direct", "framework", "accredit", "certif", "workshop", "seminar", "confer", "convent", "tradefair", "expo", "forum", "sustain", "sustainabletour", "ecotour", "responsibletravel", "volunteertravel", "green", "organ", "biodivers", "conserv", "preserv", "restor", "recycl", "wast", "pollut", "carbonfootprint", "offset", "climatechang", "adapt", "mitig", "renew", "solar", "wind", "hydro", "geotherm", "emiss", "habitat", "wildlif", "fauna", "flora", "marineecosyst", "mangrov", "sanctuari", "biospher", "geotour", "agritour", "culturalheritag", "worldheritag", "unescorecognit", "marinepark", "naturetrail", "interpretationcent", "environmentaleduc", "climateaw", "biodiversity", "habitatprotect", "wildlifeconserv", "carbonneutr", "greenbuild", "localcommun", "indigenouscultur", "ecocertif", "responsibleoper", "fairtrad", "communitybasedtour", "slowtravel", "authenticexperi", "vaccin", "immun", "malaria", "dengu", "travelclinic", "doctor", "nurs", "paramed", "pharmac", "prescript", "healthcar", "emerg", "hospital", "ambul", "evacu", "rescu", "lifeguard", "safetyinstruct", "seatbelt", "airbag", "lifeboat", "lifejacket", "hazard", "warn", "caut", "insurancepolici", "healthinsur", "travelwarn", "alert", "incidentreport", "lostandfound", "theft", "burglar", "scam", "fraud", "counterfeit", "securewebsit", "firewal", "antivir", "passportcontrol", "xraymachin", "metaldetector", "patdown", "aiassist", "chatbot", "virtualre", "augmentedre", "hologram", "wearabl", "smartdevic", "iot", "blockchain", "cryptocurr", "digitalwallet", "onlinereserv", "eticket", "mobileboardingpass", "qrcod", "biometr", "facialrecognit", "fingerprintscan", "electriccar", "hybridcar", "autonomousvehicl", "drone", "robot", "automatedcheckin", "selfservicekiosk", "contactlesspay", "wifi", "hotspot", "ethernet", "broadband", "stream", "download", "appstor", "mobileapp", "softwar", "hardwar", "firmwar", "patch", "bugfix", "userinterfac", "userexperi", "personalis", "customis", "algorithm", "analyt", "bigdataanalysi", "geoloc", "track", "beacon", "smartlock", "amusementpark", "waterpark", "funfair", "arcad", "casino", "theatr", "opera", "drama", "comedi", "music", "danc", "ballet", "puppetshow", "circu", "cinema", "movi", "documentaryfilm", "standupcomed", "artgaleri", "museumshop", "souvenir", "giftshop", "localcraft", "antiqu", "memorabilia", "postcard", "stamp", "marketplac", "bazaar", "vendor", "haggl", "negoti", "souvenirshop", "dutyfre", "kiosk", "stand", "pavilion", "stall", "boutiqu", "outlet", "warehous", "charityshop", "thriftstor", "fleamarket", "auction", "showroom", "demonstr", "perform", "orchestra", "band", "flashpack", "poshtel", "digitalnomad", "bleisur", "gapyear", "sabbat", "voluntour", "gastrotour", "wellnessretreat", "healingjourney", "pilgrimag", "religiousjourney", "spiritualtravel", "luxurylodg", "safaritour", "overlandtour", "flydriv", "citybreak", "weekendgetaway", "shorthaul", "longhaul", "multicountrytour", "roundtheworldticket", "cruiselin", "minivacation", "staycat", "daytrip", "microadventur", "slowfood", "farmstai", "packinglist", "itinerarybuild", "flightcompar", "hotelcompar", "pricealert", "consultant", "expert", "blogger", "vlogger", "podcast", "chatroom", "onlinecommun", "subscript", "membershipcard", "redempt", "upgradevouch", "seatselect", "loungeaccess", "priorityboard", "fasttrack", "globalentri", "precheck", "assignedseat", "standbi", "overbook", "cancellationpolici", "refund", "reimburs", "delay", "strike", "layov", "connectionflight", "transit", "stopov", "hub", "spoke", "allianc", "codeshar", "frequentfly", "mileag", "loyaltypoint", "reward", "tierstatu", "upgradeoption", "companionpass", "loung", "vipservic", "prioritylan", "fastlan", "dedicatedcheckin", "premiumcabin", "businessclass", "firstclass", "economyclass", "basicfar", "flexiblefar", "refundablefar", "nonrefund", "lowfar", "blackout", "peakperiod", "offpeak", "highdemand", "yield", "ancillari", "upsell", "crosssel", "dynamicpric", "bundledoff", "packag", "roam", "simcard", "esim", "satellitephon", "internetcaf", "postoffic", "mailbox", "aerogram", "courier", "deliv", "expressmail", "trackingnumb", "telephon", "landlin", "payphon", "internationalcal", "callingcard", "voicemail", "dialup", "hotlin", "helplin", "oper", "directoryassist", "translationservic", "languagebarri", "signpost", "audioguid", "videoguid", "webinar", "livestream", "broadcast", "teleconfer", "voicemailbox", "headset", "microphon", "speaker", "amplifi", "receiv", "transmit", "antenna", "router", "modem", "antispam", "phish", "encrypt", "passcod", "password", "pin", "login", "logout", "usernam", "profil", "avatar", "thumbnail", "upload", "filetransf", "bandwidth", "latenc", "firewallset", "controlpanel", "admin", "moder", "filter", "spam", "junkmail", "inbox", "outbox", "draft", "attach", "hyperlink", "url", "domain", "server", "client", "subscrib", "follow", "friend", "network", "medium", "portal", "gatewai", "index", "archiv", "librari", "bookshelf", "newspaperstand", "magazinerack", "leaflet", "handout", "noticeboard", "announc", "bulletin", "signag", "poster", "banner", "headlin", "frontpag", "column", "articl"
        ));
        this.activitiesTerms = tokenRepository.findWordsByCondition("activity");
        this.destinationTerms = tokenRepository.findWordsByCondition("destination");
        this.accomodationTerms = tokenRepository.findWordsByCondition("accomodation");
    }




    private void initializeIndex(Directory directory, Similarity similarity) throws IOException {
        try (IndexReader reader = DirectoryReader.open(directory)) {
            if (reader.numDocs() == 0) {
                reindexAllDocuments(directory, similarity);
            }
        } catch (IOException e) {
            reindexAllDocuments(directory, similarity);
        }
    }

    public void reindexAllDocuments(Directory directory, Similarity similarity) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setSimilarity(similarity);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        try (IndexWriter writer = new IndexWriter(directory, config)) {
            List<Document> allDocuments = documentRepository.findAll();

            for (Document document : allDocuments) {
                org.apache.lucene.document.Document luceneDoc = new org.apache.lucene.document.Document();

                luceneDoc.add(new StringField("id", document.getId().toString(), Field.Store.YES));
                luceneDoc.add(new TextField("title", document.getTitle(), Field.Store.YES));
                luceneDoc.add(new TextField("text", document.getText(), Field.Store.YES));

                writer.addDocument(luceneDoc);
            }
            writer.commit();
        }
    }

    public void indexDocument(Document document) throws IOException {
        indexDocumentInDirectory(document, bm25IndexDirectory, new BM25Similarity());
        indexDocumentInDirectory(document, tfidfIndexDirectory, new ClassicSimilarity());
    }

    private void indexDocumentInDirectory(Document document, Directory directory, Similarity similarity) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setSimilarity(similarity);

        try (IndexWriter writer = new IndexWriter(directory, config)) {
            org.apache.lucene.document.Document luceneDoc = new org.apache.lucene.document.Document();

            luceneDoc.add(new StringField("id", document.getId().toString(), Field.Store.YES));
            luceneDoc.add(new TextField("title", document.getTitle(), Field.Store.YES));
            luceneDoc.add(new TextField("text", document.getText().replaceAll("<[^>]*>", "").replaceAll("&nbsp;", " "), Field.Store.YES));

            writer.addDocument(luceneDoc);
        }
    }

    public List<Document> searchDocuments(String queryString, int maxResults, String similarityType) throws Exception {
        Directory directory = similarityType.equalsIgnoreCase("bm25") ? bm25IndexDirectory : tfidfIndexDirectory;

        try (IndexReader reader = DirectoryReader.open(directory)) {
            IndexSearcher searcher = new IndexSearcher(reader);

            if (similarityType.equalsIgnoreCase("bm25")) {
                searcher.setSimilarity(new BM25Similarity());
            } else {
                searcher.setSimilarity(new ClassicSimilarity());
            }

            QueryParser parser = new QueryParser("text", analyzer);
            Query query = parser.parse(queryString);

            TopDocs topDocs = searcher.search(query, 10);
            StoredFields storedFields = searcher.storedFields();

            List<Document> results = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {

                org.apache.lucene.document.Document luceneDoc = storedFields.document(scoreDoc.doc);
                Long id = Long.parseLong(luceneDoc.get("id"));
                Document doc = documentRepository.findById(id).get();
                doc.setText(doc.getText().replaceAll("<[^>]*>", "").replaceAll("&nbsp;", " "));
                results.add(doc);
            }

            return results;
        }
    }

    public List searchDocuments2(String queryString, String destination, int maxResults, String similarityType) throws Exception {
        Directory directory = similarityType.equalsIgnoreCase("bm25") ? bm25IndexDirectory : tfidfIndexDirectory;

        try (IndexReader reader = DirectoryReader.open(directory)) {
            IndexSearcher searcher = new IndexSearcher(reader);

            if (similarityType.equalsIgnoreCase("bm25")) {
                searcher.setSimilarity(new BM25Similarity());
            } else {
                searcher.setSimilarity(new ClassicSimilarity());
            }

            // Tokenize the query string using the same analyzer
            TokenStream tokenStream = analyzer.tokenStream("text", queryString);
            CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

            BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();

            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String term = charTermAttribute.toString();
                System.out.println(term);

                // Check if the term is in our important terms set
                float boost = calculateBoostForTerm(term);
                System.out.println(boost);

                // Create a boosted term query
                Term termObj = new Term("text", term);
                Query termQuery = new BoostQuery(new TermQuery(termObj), boost);

                booleanQueryBuilder.add(termQuery, BooleanClause.Occur.SHOULD);
            }
            tokenStream.close();

            // Process the destination string using the analyzer
            if (destination != null && !destination.isEmpty()) {
                TokenStream destinationTokenStream = analyzer.tokenStream("text", destination);
                destinationTokenStream.reset();

                while (destinationTokenStream.incrementToken()) {
                    String destinationTerm = charTermAttribute.toString();
                    System.out.println("Destination term: " + destinationTerm);

                    // Add destination terms with a high boost
                    Term destinationTermObj = new Term("text", destinationTerm);
                    Query destinationQuery = new BoostQuery(new TermQuery(destinationTermObj), 3.0f);

                    booleanQueryBuilder.add(destinationQuery, BooleanClause.Occur.SHOULD);
                }
                destinationTokenStream.close();
            }

            Query finalQuery = booleanQueryBuilder.build();
            System.out.println(finalQuery.toString());
            TopDocs topDocs = searcher.search(finalQuery, maxResults);
            StoredFields storedFields = searcher.storedFields();

            List results = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                org.apache.lucene.document.Document luceneDoc = storedFields.document(scoreDoc.doc);
                Long id = Long.parseLong(luceneDoc.get("id"));
                Document doc = documentRepository.findById(id).get();
                doc.setText(doc.getText().replaceAll("<[^>]*>", "").replaceAll(" ", " "));
                results.add(doc);
            }

            return results;
        }
    }


    public void deleteDocument(Long documentId) throws IOException {
        // Remove from MySQL database
        documentRepository.deleteById(documentId);

        // Remove from Lucene index
        try (IndexWriter writer = new IndexWriter(tfidfIndexDirectory, new IndexWriterConfig(analyzer))) {
            writer.deleteDocuments(new Term("id", documentId.toString()));
            writer.commit();
        }
        try (IndexWriter writer = new IndexWriter(bm25IndexDirectory, new IndexWriterConfig(analyzer))) {
            writer.deleteDocuments(new Term("id", documentId.toString()));
            writer.commit();
        }
    }


    // Utility method for getting index directory paths
    public String getBm25IndexPath() {
        return bm25IndexDirectory.toString();
    }

    public String getTfidfIndexPath() {
        return tfidfIndexDirectory.toString();
    }
    private float calculateBoostForTerm(String term) {
        // If the term is in our important terms set, boost it
        if(destinationTerms.contains(term)) return 2.2f;
        if(activitiesTerms.contains(term)) return 2.0f;
        if(accomodationTerms.contains(term)) return 2.0f;
        return 1.0f;
    }


    public  Set<String> tokenizeWithEnglishAnalyzer(String text, String category) throws IOException {
        Set<String> tokens = new HashSet<>();

        // Create an instance of EnglishAnalyzer
        try (Analyzer analyzer = new EnglishAnalyzer()) {
            // Use the analyzer to tokenize the input text
            try (TokenStream tokenStream = analyzer.tokenStream("field", text)) {
                CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
                tokenStream.reset();

                // Collect tokens
                while (tokenStream.incrementToken()) {
                    if(tokens.add(charTermAttribute.toString())){
                        tokenRepository.save(new Token(charTermAttribute.toString(),category));

                    }
                }

                tokenStream.end();
            }
        }
        System.out.println(tokens.size());
        return tokens;
    }
}