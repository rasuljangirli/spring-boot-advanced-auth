# 🛡️ Advanced Spring Boot Auth & Security System Template

Bu layihə, modern veb tətbiqlər üçün sənaye standartlarına (Production-Ready) tam uyğun, yüksək təhlükəsizlikli bir **Autentifikasiya və İdentifikasiya (IAM)** infrastrukturudur. Layihə daxilində xətasız işləyən çoxmərhələli qeydiyyat, asinxron OTP və token idarəetməsi mövcuddur.

---

##  Əsas Texnoloji Stack

* **Backend:** Java 17 / Spring Boot 3.x
* **Security:** Spring Security / JWT (JSON Web Tokens)
* **Database:** PostgreSQL / Spring Data JPA
* **Asynchronous:** Spring Async (Multi-threading for Mail)
* **Documentation:** OpenAPI 3 / Swagger UI
* **Lombok:** Kodların azaldılması üçün

---

##  Kritik Təhlükəsizlik və Biznes Funksiyaları

### 1. Asinxron OTP Doğrulama (Multi-threading)
* İstifadəçi qeydiyyatdan keçdikdən sonra əsas axını (thread) dondurmamaq üçün **ayrı bir thread-də (Asinxron)** Gmail üzərindən 6 rəqəmli doğrulama kodu göndərilir.
* Kodun etibarlılıq müddəti **90 saniyədir**. Vaxtında daxil edilərsə hesab `ACTIVE` statusuna keçir.

### 2. Sürət Limiti & Anti-Bruteforce (Rate Limiting)
* Eyni istifadəçi ard-arda maksimum **3 dəfə** yeni OTP kodu istəyə bilər (`resend-code`).
* 3-cü cəhddən sonra sistem təhlükəsizlik məqsədilə istifadəçini **1 saatlıq bloka atır** və yeni kod göndərilməsini dayandırır.

### 3. Təhlükəsiz Seans İdarəetməsi (Token Rotation & Hijack Protection)
* Hər bir istifadəçi eyni anda maksimum **5 fərqli cihazdan** (5 aktiv Refresh Token) daxil ola bilər. 6-cı cihaz daxil olduqda `FIFO` məntiqi ilə ən köhnə token silinir.
* **Session Hijacking Müdafiəsi:** Əgər `/refresh-token` endpointinə gələn sorğuda cihaz məlumatı (`User-Agent`) bazadakı məlumatla üst-üstə düşmürsə, sistem hücumu ehtimalını dəyərləndirir və təhlükəsizlik üçün **həmin istifadəçinin bütün aktiv seanslarını (bütün refresh tokenlərini) daxili bazadan silərək** hər kəsi sistemdən atır.

---

##  Layihə Arxaturası (Layered Architecture)

Layihə **3-Tier (Təbəqəli) Memarlıq** modelinə əsaslanır:
* `controller` & `dto`: Giriş-çıxış qapısı, validasiya təbəqəsi.
* `service`: Bütün biznes məntiqi, təhlükəsizlik yoxlanışları və hesablamalar.
* `repository` & `model`: Verilənlər bazası (PostgreSQL) əməliyyatları.
* `security` & `task`: Spring Security filtrləri, JWT util sinifləri və arxa planda işləyən asinxron tapşırıqlar (Scheduled Tasks).

---

## 🛠 Yerli Mühitdə İşə Salmaq (Local Setup)

1. Bu repozitoriyanı klonlayın:
   ```bash
   git clone [https://github.com/mehemmedfettahzade/LAYIHE_ADI.git](https://github.com/mehemmedfettahzade/LAYIHE_ADI.git)