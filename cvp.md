# 30 Yazılım Mimarisi ve Geliştirme Soruları ve Cevapları

## 🚀 Microservices & Distributed Systems

### 1. Event-Driven mimaride bir event'in tüketilmemesi veya yanlış işlenmesi durumunda neler yapılabilir?

**Cevap:**
- **Dead Letter Queue (DLQ)**: İşlenemeyen mesajları ayrı bir kuyruğa taşıyarak sonradan inceleme
- **Retry Mekanizması**: Exponential backoff ile yeniden deneme
- **İdempotent İşlemler**: Aynı mesajın birden fazla işlenmesi durumunda tutarlılık sağlama
- **Event Sourcing**: Tüm olayları logda saklayarak gerektiğinde sistemi yeniden oluşturma
- **Monitoring ve Alerting**: Hatalı işlemleri tespit etme ve bildirim
- **Circuit Breaker**: Sürekli hata veren servisleri geçici olarak devre dışı bırakma
- **Compensation Transactions**: Hatalı işlemlerin etkilerini geri alma

### 2. Microservices ile çalışan bir sistemde database-per-service modelinin avantajları ve dezavantajları nelerdir?

**Cevap:**
**Avantajlar:**
- Bağımsız ölçeklendirme imkanı
- Teknoloji çeşitliliği (polyglot persistence)
- Servisler arası düşük bağımlılık
- İzolasyon ve hataların sınırlandırılması
- Daha net domain sınırları

**Dezavantajlar:**
- Veri tutarlılığı zorlukları
- Operasyonel karmaşıklık
- Veri tekrarı ve senkronizasyon sorunları
- Servisler arası karmaşık sorgular
- Daha yüksek altyapı maliyeti

### 3. Dağıtık sistemlerde veri tutarlılığı nasıl sağlanır? Eventual consistency nedir?

**Cevap:**
**Veri tutarlılığı yöntemleri:**
- İki Aşamalı Commit (2PC): Tüm katılımcıların işlemi onaylaması
- Saga Pattern: İşlemleri atomik adımlara bölme ve telafi edici işlemler
- Event Sourcing: Durum değişikliklerini olay olarak kaydetme
- CQRS: Okuma ve yazma işlemlerini ayırma
- Distributed Transactions: Koordine edilmiş işlemler

**Eventual Consistency (Nihai Tutarlılık)**: Sistem geçici olarak tutarsız olabilir, ancak yeni güncellemeler olmadığında ve yeterince zaman geçtiğinde tüm sistem tutarlı hale gelir. Yüksek erişilebilirlik sağlar ancak anlık tutarlılıktan ödün verir.

### 4. Bir microservisin başka bir microservise bağımlılığı fazla ise nasıl bir yeniden yapılandırma önerirsiniz?

**Cevap:**
- Domain sınırlarını yeniden değerlendirme
- API kompozisyon katmanı ekleme
- Event-driven mimari ile asenkron iletişime geçme
- Bounded Context prensiplerini uygulama
- Gerekli verileri replike etme
- Facade pattern ile API çağrılarını birleştirme
- Service mesh kullanarak iletişimi yönetme
- Önbellekleme ile servis çağrılarını azaltma

### 5. Tek bir transaction içinde birden fazla microservice çağırmanız gerekiyor. Veri tutarlılığı için hangi yöntemleri önerirsiniz?

**Cevap:**
- **Saga Pattern**: 
  - Choreography-based: Servisler event'lerle iletişim kurar
  - Orchestration-based: Merkezi koordinatör servis
- **Outbox Pattern**: Lokal transaction ve mesaj göndermeyi birleştirme
- **Event Sourcing**: Durum değişikliklerini olaylar olarak kaydetme
- **Try-Confirm/Cancel (TCC)**: İşlemi hazırlık, onay ve iptal aşamalarına bölme
- **Eventual Consistency + Compensation**: Asenkron işlem ve hata durumunda telafi
- **API Composition**: Kompozisyon servisi ile koordinasyon

## 🛠️ Spring & Java Core

### 6. Spring Boot'ta bir Service bileşeni içinde state (durum) tutmanın olası riskleri nelerdir?

**Cevap:**
- **Thread Safety Sorunları**: Singleton servislerde race condition
- **Ölçeklenebilirlik Sorunları**: Çoklu instance'larda tutarsızlık
- **Test Edilebilirlik Sorunları**: Testlerin birbirini etkilemesi
- **Bellek Sızıntıları**: Sürekli büyüyen koleksiyonlar
- **Servis Yaşam Döngüsü Sorunları**: Container restart'ında state kaybı
- **Single Responsibility İhlali**: Servisin fazla sorumluluk üstlenmesi

Çözüm olarak state'i veritabanı, Redis gibi harici depolama sistemlerinde veya session-scoped bean'lerde tutmak önerilir.

### 7. Java'da nesnelerin Garbage Collector tarafından temizlenmesini nasıl garanti edersiniz?

**Cevap:**
- **Referansları null yapma**: Kullanılmayan nesneleri erişilemez hale getirme
- **try-with-resources**: AutoCloseable nesneleri otomatik kapatma
- **WeakReference/SoftReference kullanma**: GC'nin nesneleri daha kolay toplamasına izin verme
- **Döngüsel referansları önleme**: Nesnelerin birbirine referans vermesini engelleme
- **Memory leak'leri önleme**: Statik koleksiyonlara dikkat etme
- **JVM parametrelerini ayarlama**: GC davranışını optimize etme

GC'nin ne zaman çalışacağı JVM kontrolünde olduğundan tam garanti yoktur, iyi kod pratikleri uygulamak önemlidir.

### 8. Spring AOP ile bir metod çağrılmadan önce veya sonra müdahale etmek için hangi yapıları kullanabilirsiniz?

**Cevap:**
- **@Before**: Metod çağrılmadan önce çalışır
- **@After**: Metod çağrıldıktan sonra (sonuç ne olursa olsun) çalışır
- **@AfterReturning**: Metod başarıyla tamamlandığında çalışır
- **@AfterThrowing**: Metod istisna fırlattığında çalışır
- **@Around**: Metodu tamamen sarmalayarak öncesi ve sonrasında çalışır
- **@Pointcut**: Tekrar kullanılabilir pointcut tanımlamaları oluşturur

```java
@Around("execution(* com.example.service.*.*(..))")
public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
    // Metod çağrılmadan önce
    Object result = joinPoint.proceed(); // Metodu çağır
    // Metod çağrıldıktan sonra
    return result;
}
```

### 9. Transactional annotation'ı kullandığınız bir serviste performansı artırmak için nelere dikkat etmelisiniz?

**Cevap:**
- **Transaction kapsamını daraltma**: Sadece gerekli işlemleri transaction içine alma
- **Doğru izolasyon seviyesi seçme**: İhtiyaca uygun en düşük seviyeyi kullanma (READ_COMMITTED gibi)
- **Read-only flag kullanma**: Sadece okuma yapan metodlarda `readOnly=true`
- **Uygun propagasyon davranışı seçme**: İç içe transaction'lar için doğru propagation
- **N+1 sorgu probleminden kaçınma**: Lazy loading kullanırken dikkatli olma
- **Transaction timeout ayarlama**: Uzun süren transaction'ları sınırlama
- **Batch işlemler için optimizasyon**: Büyük veri setlerinde batch processing
- **Transaction içinde uzun işlemlerden kaçınma**: HTTP çağrıları, dosya I/O gibi

### 10. Spring Context içinde birden fazla Bean aynı anda mevcutsa hangisinin enjekte edileceğini nasıl belirlersiniz?

**Cevap:**
- **@Primary**: Bir bean'i öncelikli olarak işaretleme
- **@Qualifier**: Enjeksiyon noktasında hangi bean'in kullanılacağını belirtme
- **@Resource**: Doğrudan bean adıyla enjeksiyon yapma
- **Bean adı ile autowiring**: Değişken adı bean adıyla aynı olduğunda
- **Constructor injection ile qualifier**: Constructor parametresinde belirtme
- **@Profile**: Farklı ortamlar için farklı implementasyonlar
- **@Conditional**: Belirli koşullara bağlı bean oluşturma
- **@Order**: Koleksiyon olarak enjekte edildiğinde sıralama

```java
@Autowired
@Qualifier("specificServiceBean")
private MyService myService;
```

## 🛡️ Güvenlik & Authentication

### 11. Bir REST API'de kimlik doğrulama ve yetkilendirme için hangi güvenlik mekanizmalarını kullanabilirsiniz?

**Cevap:**
- **Basic Authentication**: Kullanıcı adı/şifre (sadece HTTPS ile güvenli)
- **API Keys**: İstemcilere özel anahtarlar
- **OAuth 2.0**: Üçüncü taraf uygulamalar için yetkilendirme framework'ü
- **JWT (JSON Web Tokens)**: İmzalı ve self-contained tokenlar
- **OpenID Connect**: OAuth 2.0 üzerine kimlik doğrulama katmanı
- **HMAC Authentication**: İçerik imzalama
- **Mutual TLS (mTLS)**: İki yönlü sertifika doğrulama
- **Role-Based Access Control (RBAC)**: Rol tabanlı yetkilendirme
- **Multi-Factor Authentication (MFA)**: Çok faktörlü doğrulama
- **Rate Limiting**: İstek sınırlama

### 12. CSRF saldırılarını önlemek için hangi yöntemleri kullanırsınız?

**Cevap:**
- **CSRF Token**: Her form ve AJAX isteğine benzersiz token ekleme
- **SameSite Cookie Attribute**: Cross-site isteklerde çerez gönderimini kısıtlama
- **Custom Request Headers**: AJAX isteklerinde özel header'lar kullanma
- **Double Submit Cookie**: Token'ı hem cookie hem request parametresi olarak gönderme
- **Origin/Referer Header Kontrolü**: İsteğin geldiği kaynağı doğrulama
- **POST İstekleri Kullanma**: GET yerine POST, PUT gibi metodlar tercih etme
- **Re-authentication**: Kritik işlemler için yeniden kimlik doğrulama
- **Secure ve HttpOnly Cookie Özellikleri**: Çerez güvenliğini artırma

### 13. OAuth 2.0 ve JWT arasındaki farklar nelerdir?

**Cevap:**
- **Amaç**: 
  - OAuth 2.0: Yetkilendirme protokolü
  - JWT: Token formatı
- **İşlevsellik**:
  - OAuth 2.0: Yetkilendirme akışlarını tanımlar
  - JWT: Bilgiyi kodlama, imzalama ve şifreleme standardı
- **Yapı**:
  - OAuth 2.0: Roller ve etkileşimler tanımlar
  - JWT: Header, Payload ve Signature bölümlerinden oluşur
- **Doğrulama**:
  - OAuth 2.0: Genellikle authorization server'a sorgu
  - JWT: İmza yerel olarak doğrulanabilir
- **İlişki**: JWT, OAuth 2.0 içinde token formatı olarak kullanılabilir

### 14. Bir web uygulamasında güvenli oturum yönetimi nasıl sağlanır?

**Cevap:**
- **Güçlü Session ID**: Yeterince uzun, tahmin edilemez ID'ler
- **Güvenli Cookie Yapılandırması**: Secure, HttpOnly, SameSite özellikleri
- **Session Timeout**: Mutlak ve hareketsizlik timeout'ları
- **Güvenli Logout**: Sunucu tarafında session invalidation
- **Session Fixation Koruması**: Login sonrası yeni session ID
- **Concurrent Session Kontrolü**: Eşzamanlı oturum sınırlaması
- **CSRF Koruması**: Token kullanımı
- **HTTPS Kullanımı**: Tüm oturum trafiğini şifreleme
- **MFA**: Çok faktörlü kimlik doğrulama
- **Şüpheli Aktivite İzleme**: Login denemeleri ve IP değişikliklerini takip etme

### 15. Bir API'nin rate limit'ini aşan bir istemciye nasıl tepki verirsiniz?

**Cevap:**
- **HTTP 429 Status Code**: "Too Many Requests" yanıtı
- **Retry-After Header**: Yeniden deneme süresi bildirme
- **RateLimit Header'ları**: Limit, kalan hak ve reset zamanı bilgisi
- **Açıklayıcı Hata Mesajı**: Kullanıcı dostu bilgilendirme
- **Kademeli Throttling**: Aşım tekrarlandıkça daha uzun bekleme süreleri
- **Farklı Limit Politikaları**: Endpoint, kullanıcı veya plan bazlı limitler
- **Alternatifler Sunma**: Batch API veya webhook entegrasyonları

```http
HTTP/1.1 429 Too Many Requests
Retry-After: 60
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 0
X-RateLimit-Reset: 1623456789
```

## 🖥️ Performans & Optimizasyon

### 16. Bir Java uygulamasında performansı artırmak için kullanılan bellek yönetimi teknikleri nelerdir?

**Cevap:**
- **Object Pooling**: Sık kullanılan nesneleri havuzda tutma
- **Immutable Objects**: Değişmez nesneler kullanarak thread safety
- **String Interning**: String havuzunu etkin kullanma
- **StringBuilder**: String concatenation için StringBuilder kullanma
- **Primitive Tipler**: Wrapper yerine primitive tipler tercih etme
- **Uygun Koleksiyon**: Kullanım senaryosuna göre doğru koleksiyon seçimi
- **Lazy Initialization**: Nesneleri ihtiyaç anında oluşturma
- **Weak/Soft References**: GC'nin bellek baskısında nesneleri toplamasına izin verme
- **JVM Ayarları**: Heap boyutu ve GC algoritması optimizasyonu
- **Memory Leak Önleme**: Statik koleksiyonlar ve kapatılmayan kaynaklara dikkat

### 17. Bir SQL sorgusunun performansını artırmak için hangi indeksleme stratejilerini kullanırsınız?

**Cevap:**
- **WHERE Koşullarına İndeks**: Sık filtrelenen sütunlara indeks
- **Covering İndeks**: Sorgunun tüm ihtiyaçlarını karşılayan indeks
- **Composite İndeks**: Birlikte kullanılan sütunlara doğru sırada indeks
- **Partial/Filtered İndeks**: Belirli koşulları sağlayan satırlar için indeks
- **Function-Based İndeks**: Fonksiyon sonuçları üzerinde indeks
- **JOIN Sütunlarına İndeks**: JOIN koşullarında kullanılan sütunlar
- **ORDER BY/GROUP BY İndeksi**: Sıralama ve gruplama sütunlarına indeks
- **İndeks Bakımı**: Düzenli rebuild/reorganize ve istatistik güncelleme
- **İndeks Seçiciliği**: Yüksek kardinaliteli sütunlara öncelik verme
- **Gereksiz İndekslerden Kaçınma**: Küçük tablolar, düşük seçicilik, sık güncellenen sütunlar

### 18. Spring Boot uygulamalarında bellek sızıntısını nasıl önleyebilirsiniz?

**Cevap:**
- **Singleton Bean'lerde State Tutmama**: Büyüyen koleksiyonlardan kaçınma
- **Kaynakları Düzgün Kapatma**: try-with-resources kullanımı
- **ThreadLocal Temizleme**: Kullanım sonrası remove() çağrısı
- **Event Listener'ları Kaldırma**: @PreDestroy ile cleanup
- **Connection Pooling Yapılandırması**: Uygun pool boyutu ve timeout
- **Cache TTL ve Boyut Sınırlaması**: Önbellek büyümesini kontrol etme
- **Lazy Loading**: Gereksiz bean'leri erken yüklememe
- **HTTP Session Yönetimi**: Session timeout ve büyük nesnelerden kaçınma
- **Asenkron İşlemlerde Thread Pool Yönetimi**: Doğru boyutlandırma
- **ORM İlişkilerinde Lazy Loading**: N+1 sorgu probleminden kaçınma
- **Heap Dump Analizi**: Düzenli bellek kullanımı incelemesi

### 19. Yüksek trafikli bir uygulamada eşzamanlı kullanıcı isteklerini en iyi şekilde yönetmek için hangi yöntemleri önerirsiniz?

**Cevap:**
- **Asenkron İşleme**: Non-blocking I/O ve reactive programming
- **Thread Pool Optimizasyonu**: İş yüküne göre thread havuzu boyutlandırma
- **Connection Pooling**: Veritabanı ve HTTP bağlantı havuzları
- **Caching**: Uygulama ve dağıtık önbellek kullanımı
- **Yük Dengeleme**: Çoklu sunucu arasında trafik dağıtımı
- **Rate Limiting**: İstemci bazında istek sınırlaması
- **Circuit Breaker**: Başarısız servis çağrılarını yönetme
- **Microservices**: Bağımsız ölçeklendirilebilen servisler
- **Message Queue**: Asenkron işleme için kuyruk sistemleri
- **Stateless Tasarım**: Durum bilgisini client tarafında tutma
- **Database Sharding**: Veritabanı yükünü dağıtma

### 20. Docker ile oluşturulmuş bir uygulamanın performansını artırmak için neler yapılabilir?

**Cevap:**
- **İmaj Boyutunu Küçültme**: Multi-stage build ve alpine imajlar
- **JVM Optimizasyonu**: Container-aware JVM ayarları
- **Kaynak Sınırlamaları**: CPU ve bellek limitleri
- **Ağ Optimizasyonu**: Host network mode veya uygun driver seçimi
- **Depolama Optimizasyonu**: tmpfs ve volume kullanımı
- **Önbellekleme**: Build cache ve layer optimizasyonu
- **Sağlık Kontrolleri**: HEALTHCHECK talimatı ve graceful shutdown
- **Logging Optimizasyonu**: JSON format ve log seviyesi ayarları
- **Base Image Seçimi**: Minimal ve güncel imajlar
- **Docker Buildkit**: Paralel build süreçleri
- **Monitoring**: Container performans izleme

## 🔗 API & Network

### 21. Bir API'ye gelen yüksek trafikte gecikmeleri en aza indirmek için hangi yöntemleri kullanırsınız?

**Cevap:**
- **Caching**: Response caching, HTTP caching, CDN kullanımı
- **Asenkron İşleme**: Non-blocking I/O ve reactive framework'ler
- **Yük Dengeleme**: Trafik dağıtımı ve auto-scaling
- **Database Optimizasyonu**: Connection pooling ve query optimizasyonu
- **API Gateway**: Request routing ve aggregation
- **Veri Transfer Optimizasyonu**: Compression, pagination, partial response
- **Backend İşlem Optimizasyonu**: Paralel işleme ve batch processing
- **Circuit Breaker**: Hata yönetimi ve fallback yanıtlar
- **Edge Computing**: İşlemleri kullanıcıya yakın noktalarda gerçekleştirme
- **Monitoring ve Profiling**: Darboğazları tespit etme

### 22. Bir API'nin response süresini azaltmak için neler yapılabilir?

**Cevap:**
- **Veritabanı Sorgu Optimizasyonu**: İndeksler, explain plan analizi, N+1 çözümü
- **Caching**: Uygulama seviyesi, HTTP ve dağıtık önbellek
- **Response Payload Optimizasyonu**: Gereksiz alanları kaldırma, pagination, compression
- **Asenkron İşleme**: Non-blocking I/O ve paralel processing
- **Kod Optimizasyonu**: Algoritma iyileştirmeleri ve bellek kullanımı
- **Connection Pooling**: Veritabanı ve HTTP client bağlantı havuzları
- **API Tasarım Optimizasyonu**: GraphQL, batch endpoints, composite API
- **Serialization Optimizasyonu**: Hızlı serileştirme kütüphaneleri
- **JVM ve Uygulama Sunucusu Ayarları**: GC, heap size, thread pool optimizasyonu
- **Monitoring ve Profiling**: Hotspot analizi ve distributed tracing

### 23. Bir uygulamanın veri transferini optimize etmek için hangi veri sıkıştırma teknikleri kullanılabilir?

**Cevap:**
- **HTTP Compression**: GZIP, Brotli, Deflate
- **Veri Formatı Optimizasyonu**: JSON minification, Protocol Buffers, MessagePack
- **İmaj Sıkıştırma**: WebP, AVIF, responsive images
- **Video Sıkıştırma**: H.265/HEVC, AV1, VP9
- **Metin Sıkıştırma**: LZMA, Zstandard, LZ4
- **Binary JSON Formatları**: BSON, UBJSON, Smile
- **Delta Encoding**: Sadece değişiklikleri gönderme
- **Adaptive Compression**: İstemci özelliklerine göre sıkıştırma
- **GraphQL**: Seçici veri alma
- **Chunked Transfer**: Büyük yanıtları parçalara bölme

### 24. WebSocket ve REST API arasındaki farklar nelerdir? Hangi durumda hangisini kullanmalıyız?

**Cevap:**
**Temel Farklar:**
- **İletişim Modeli**: REST (request-response) vs WebSocket (full-duplex)
- **Bağlantı Durumu**: REST (stateless) vs WebSocket (stateful)
- **Protokol**: REST (HTTP/HTTPS) vs WebSocket (ws/wss)
- **Overhead**: REST (her istekte HTTP header) vs WebSocket (minimal header)
- **Caching**: REST (HTTP cache desteği) vs WebSocket (doğrudan cache yok)

**REST API Kullanım Senaryoları:**
- CRUD işlemleri
- Durumsuz işlemler
- Aralıklı istek-yanıt modeli
- Önbelleğe alınabilir veriler
- Geniş istemci desteği gerektiren durumlar

**WebSocket Kullanım Senaryoları:**
- Gerçek zamanlı uygulamalar (chat, oyunlar)
- Düşük gecikme gerektiren işlemler (borsa verileri)
- Sunucudan istemciye bildirimler (push notifications)
- Yüksek frekanslı veri akışı (IoT sensörleri)
- İşbirliği araçları (çoklu kullanıcılı düzenleme)

### 25. Bir API Gateway'in rolü nedir? Hangi durumlarda API Gateway kullanmalıyız?

**Cevap:**
**API Gateway Rolleri:**
- İstek yönlendirme (request routing)
- API kompozisyonu (birden fazla servis çağrısını birleştirme)
- Protokol dönüşümü (REST → gRPC gibi)
- Kimlik doğrulama ve yetkilendirme
- Rate limiting ve throttling
- Caching
- Request/response dönüşümü
- Logging ve monitoring
- Circuit breaking
- SSL termination
- Yük dengeleme

**API Gateway Kullanım Senaryoları:**
- Mikroservis mimarisi
- Çoklu istemci desteği (web, mobil, IoT)
- Merkezi güvenlik gereksinimleri
- API monetizasyonu
- Legacy sistemlerle entegrasyon
- Cross-cutting concerns (loglama, monitoring)
- Coğrafi dağıtım
- A/B testing ve canary releases
- Multi-tenant uygulamalar

## 📊 Veri & Database

### 26. Bir veritabanı tasarımında normalizasyon ve denormalizasyon arasındaki farklar nelerdir? Hangi durumlarda denormalizasyon tercih edilmelidir?

**Cevap:**
**Temel Farklar:**
- **Amaç**: Normalizasyon (veri tutarlılığı) vs Denormalizasyon (performans)
- **Veri Tekrarı**: Normalizasyon (minimize) vs Denormalizasyon (izin verir)
- **JOIN İşlemleri**: Normalizasyon (çok) vs Denormalizasyon (az)
- **Güncelleme**: Normalizasyon (basit) vs Denormalizasyon (karmaşık)
- **Depolama**: Normalizasyon (verimli) vs Denormalizasyon (daha fazla alan)

**Denormalizasyon Tercih Edilmeli:**
- Yüksek okuma/düşük yazma oranı olan sistemlerde
- Karmaşık sorgular ve raporlama gerektiren durumlarda
- Yüksek performans gereksinimleri olduğunda
- Sık kullanılan hesaplanmış değerler için
- Veri ambarları ve OLAP sistemlerinde
- Mikroservis mimarisinde servis özerkliği için
- Zaman serisi verileri için

### 27. Bir veritabanında indeksler nasıl çalışır? Hangi durumlarda indeks oluşturmalı veya oluşturmamalıyız?

**Cevap:**
**İndeks Çalışma Prensibi:**
- Veritabanı indeksleri, genellikle B-Tree yapısında organize edilir
- Kök, iç düğümler ve yaprak düğümlerden oluşur
- Sorgu motoruna hızlı erişim için referans sağlar
- Tam tablo taraması yerine indeks taraması yapılır

**İndeks Oluşturulması Gereken Durumlar:**
- Sık kullanılan WHERE koşullarında
- JOIN işlemlerinde kullanılan sütunlarda
- Foreign key sütunlarında
- ORDER BY ve GROUP BY kullanılan sütunlarda
- Unique kısıtlaması olan sütunlarda
- Büyük tablolarda sık sorgulanan sütunlarda

**İndeks Oluşturulmaması Gereken Durumlar:**
- Küçük tablolarda (birkaç yüz satır)
- Sık güncellenen sütunlarda (yazma performansı kritikse)
- Düşük seçicilik (low cardinality) sütunlarında (cinsiyet gibi)
- Çok geniş veri tipleri üzerinde (TEXT, BLOB)
- Nadiren sorgulanan sütunlarda
- Zaten başka bir indeksin parçası olan sütunlarda

### 28. Bir veritabanı sorgusunun performansını artırmak için hangi optimizasyon teknikleri kullanılabilir?

**Cevap:**
- **İndeks Optimizasyonu**: Doğru sütunlara indeks oluşturma
- **Sorgu Yapısı İyileştirme**: Gereksiz JOIN'lerden kaçınma, sadece gerekli sütunları seçme
- **EXPLAIN Kullanımı**: Sorgu planlarını analiz etme
- **Uygun Veri Tipleri**: Doğru boyut ve tipleri seçme
- **Partitioning**: Verileri mantıksal parçalara bölme
- **Denormalizasyon**: Stratejik olarak veri tekrarına izin verme
- **Materialized View**: Karmaşık sorguların sonuçlarını önceden hesaplama
- **Veritabanı Yapılandırması**: Buffer pool, cache ve çalışma belleği ayarları
- **Batch İşlemler**: Çoklu insert/update işlemlerini birleştirme
- **Prepared Statement**: Sorgu planı önbelleğinden faydalanma
- **UNION ALL vs UNION**: Gereksiz tekrar kontrolünden kaçınma
- **EXISTS vs IN**: Büyük veri setlerinde EXISTS tercih etme
- **Window Functions**: Tekrarlı alt sorgular yerine kullanma
- **Geçici Tablolar**: Karmaşık sorguları basitleştirme

### 29. Bir veritabanı tasarımında sharding ne demektir? Ne zaman ve nasıl uygulanmalıdır?

**Cevap:**
**Sharding Nedir:**
Sharding, büyük bir veritabanını daha küçük, bağımsız parçalara (shard'lara) bölen yatay ölçeklendirme tekniğidir. Her shard aynı şemaya sahiptir ancak farklı veri alt kümelerini içerir.

**Sharding Stratejileri:**
- **Range Based**: Belirli aralıklara göre bölme (user_id 1-1000 shard1, 1001-2000 shard2)
- **Hash Based**: Shard key'in hash değerine göre dağıtım
- **Directory Based**: Merkezi lookup tablosu ile yönetim
- **Geo-Based**: Coğrafi konuma göre bölme

**Ne Zaman Uygulanmalı:**
- Veri boyutu tek sunucu kapasitesini aştığında
- Yüksek yazma/okuma throughput gerektiğinde
- Coğrafi dağıtım gerektiğinde
- Yüksek erişilebilirlik kritik olduğunda
- Dikey ölçeklendirme limitlerine ulaşıldığında

**Nasıl Uygulanmalı:**
1. **Hazırlık**: Veri erişim desenlerini analiz etme, shard key seçimi
2. **Mimari**: Shard router geliştirme veya proxy kullanma
3. **Veri Taşıma**: Minimum kesinti ile veri bölümleme
4. **Yönetim**: Rebalancing, monitoring ve yeni shard ekleme stratejileri

**Zorluklar ve Çözümler:**
- Cross-shard sorgular için paralel sorgulama
- Distributed transaction için saga pattern
- Schema değişiklikleri için otomatik yönetim
- Hotspot önleme için dengeli dağılım stratejileri

### 30. NoSQL ve SQL veritabanları arasındaki temel farklar nelerdir? Hangi durumlarda hangisini tercih etmeliyiz?

**Cevap:**
**Temel Farklar:**

| Özellik | SQL | NoSQL |
|---------|-----|-------|
| Veri Modeli | Tablo yapısı, önceden tanımlı şema | Esnek şema, çeşitli modeller (doküman, anahtar-değer, sütun, graf) |
| Ölçeklenebilirlik | Genelde dikey | Doğal yatay ölçeklendirme |
| Tutarlılık | ACID özellikleri | BASE özellikleri, eventual consistency |
| Sorgu Dili | Standart SQL | Veritabanına özgü API'ler |
| Şema | Katı şema | Esnek şema veya şemasız |
| İlişkiler | Foreign key ile | Genelde uygulama seviyesinde |

**SQL Tercih Edilmeli:**
- Karmaşık ilişkiler ve sorgular gerektiren uygulamalarda
- ACID garantileri kritik olduğunda (finansal işlemler)
- Şema yapısı önceden iyi tanımlandığında
- Veri bütünlüğü çok önemli olduğunda
- Karmaşık raporlama gerektiren sistemlerde

**NoSQL Tercih Edilmeli:**
- Büyük hacimli, hızlı büyüyen veriler için
- Esnek şema gerektiren uygulamalarda
- Yüksek yazma/okuma throughput gerektiren durumlarda
- Yatay ölçeklenebilirlik öncelikli olduğunda
- Özel veri modelleri gerektiren durumlarda:
  - Doküman tabanlı: İçerik yönetimi, e-ticaret katalogları
  - Anahtar-değer: Önbellekleme, oturum yönetimi
  - Sütun tabanlı: Büyük veri analizi, log verileri
  - Graf tabanlı: Sosyal ağlar, öneri motorları

**Hibrit Yaklaşım:**
Modern uygulamalar genellikle polyglot persistence (çoklu veritabanı) yaklaşımı kullanır:
- Mikroservislerde farklı veritabanları
- CQRS: Yazma için SQL, okuma için NoSQL
- Farklı veri türleri için farklı veritabanları

Seçim yaparken veri yapısı, ölçeklenebilirlik gereksinimleri, tutarlılık ihtiyaçları ve sorgu karmaşıklığı göz önünde bulundurulmalıdır.