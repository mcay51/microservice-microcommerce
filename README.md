# microservice-microcommerce

Bu proje, mikroservis mimarisini uygulamalı olarak öğrenmenize yardımcı olacak bir e-ticaret simülasyonudur. Proje, Spring Boot, Docker, PostgreSQL, NGINX, Eureka, Resilience4j ve merkezi konfigürasyon yönetimi gibi teknolojileri kullanarak mikroservislerin nasıl çalıştığını ve bu teknolojilerin nasıl entegre edildiğini göstermektedir.

## Proje Bileşenleri

### Uygulama Sunucuları

- **Product Service**: Ürün bilgilerini yöneten mikroservis.
- **Order Service**: Sipariş işlemlerini yöneten mikroservis.
- **Stock Service**: Stok yönetimini sağlayan mikroservis.
- **Configuration Service**: Merkezi konfigürasyon yönetimi yapan mikroservis.
- **API Gateway**: Mikroservislerin dış dünyaya açıldığı geçiş noktası.
- **Eureka Server**: Servislerin birbirini keşfetmesini sağlayan hizmet kayıt merkezi.

### Veritabanı

- **PostgreSQL**: Veritabanı yönetim sistemi.

### Mesaj Kuyruğu

- **RabbitMQ**: Servisler arası iletişim ve mesajlaşma için kullanılır.

### Yük Dengeleme ve Reverse Proxy

- **NGINX**: Yük dengeleme ve reverse proxy olarak kullanılır.

### İzleme ve Sağlık Kontrolü

- **Actuator**: Uygulamanın sağlık durumunu ve metriklerini izlemek için kullanılır.

### Küresel Konfigürasyon Yönetimi

- **Spring Cloud Config**: Konfigürasyonların merkezi bir yerde yönetilmesini sağlar.

## Kurulum

### Gereksinimler

- Java 11 veya üzeri
- Docker
- PostgreSQL
- RabbitMQ
- Maven
- Git

### Adım Adım Kurulum

1. **Projeyi Klonlayın**

    ```bash
    git clone https://github.com/kullaniciAdi/microservice-microcommerce.git
    cd microservice-microcommerce
    ```

2. **Docker İmajlarını Oluşturun ve Çalıştırın**

    Docker Compose ile tüm servisleri ayağa kaldırın:

    ```bash
    docker-compose up --build
    ```

3. **Veritabanı Yapılandırması**

    PostgreSQL veritabanını ve tabloları oluşturun.
    `src/main/resources/db/migration` altında gerekli SQL dosyalarını bulun ve uygulayın.

4. **Eureka Server'i Çalıştırın**

    `eureka-server` uygulamasını çalıştırın.

5. **Konfigürasyon Servisini Çalıştırın**

    `config-server` uygulamasını çalıştırın ve `application.yml` dosyasını doğru şekilde yapılandırın.

6. **Diğer Mikroservisleri Çalıştırın**

    `product-service`, `order-service` ve `stock-service` uygulamalarını çalıştırın.

7. **API Gateway'i Çalıştırın**

    `api-gateway` uygulamasını çalıştırın.

8. **NGINX Konfigürasyonunu Yapılandırın**

    `nginx.conf` dosyasını NGINX servisi için uygun şekilde yapılandırın.

9. **RabbitMQ Konfigürasyonunu Yapılandırın**

    RabbitMQ servisinin ayarlarını yapılandırın.

10. **Actuator ile Sağlık Kontrollerini Yapın**

    Uygulama sağlık kontrol uç noktalarını test edin.

## Kullanılan Teknolojiler

- **Spring Boot**: Mikroservis geliştirmek için kullanılan framework.
- **Docker**: Konteyner yönetimi ve dağıtımı için kullanılır.
- **PostgreSQL**: Veritabanı yönetim sistemi.
- **RabbitMQ**: Mesaj kuyruğu sistemi.
- **NGINX**: Yük dengeleme ve reverse proxy.
- **Eureka**: Servis keşfi için kullanılır.
- **Spring Cloud Config**: Merkezi konfigürasyon yönetimi.
- **Actuator**: İzleme ve sağlık kontrolü için kullanılır.
- **Resilience4j**: Circuit breaker ve diğer dayanıklılık çözümleri.

## Proje Yapısı

- **product-service**: Ürün yönetimi mikroservisi.
- **order-service**: Sipariş yönetimi mikroservisi.
- **stock-service**: Stok yönetimi mikroservisi.
- **config-server**: Merkezi konfigürasyon yönetim servisi.
- **eureka-server**: Servis kayıt ve keşif merkezi.
- **api-gateway**: API geçiş noktası.
- **nginx**: Yük dengeleme ve proxy.

## Notlar

- Mikroservislerin her biri ayrı bir portta çalışır ve birbirleriyle HTTP API'ler aracılığıyla iletişim kurar.
- Konfigürasyon servisinde değişiklik yapıldığında, diğer mikroservisler otomatik olarak güncellenir.

