# Spring Boot Advanced Authentication System
Youtube kanalımda dərs videolarından yararlanmaq üçün [klikləyin](https://www.youtube.com/@rasul_jangirli)

Medium hesabımda məqalələrimlə tanış olmaq üçün [klikləyin](https://medium.com/@rasuljangirli)

Layihədə asinxron OTP göndərilməsi, rate limiting, refresh token idarəetməsi, session hijacking müdafiəsi və avtomatik token təmizləmə mexanizmləri tətbiq edilmişdir.
Bu layihə əsas götürülərək, üzərində bir çox real layihə sistemləri qurula bilər.

---

## Biznes Məntiqi və Memarlıq Həlləri (Architectural Solutions)

### 1. Non-Blocking Asinxron OTP & Thread Pool
* **Problem:** İstifadəçi qeydiyyat düyməsinə basdıqda, xarici SMTP poçt serverinin (Gmail) cavab vermə gecikməsi (bəzən 2-5 saniyə çəkir) Tomcat-in əsas HTTP sorğu axınını (Thread) bloklayır. Bu, sistemdə süni sıxlığa və pis istifadəçi təcrübəsinə yol açır.
* **Həlli:** Layihə daxilində qeydiyyat prosesini tamamilə asinxronlaşdırdım. İstifadəçinin əsas məlumatları bazaya yazılan kimi əsas thread azad olunur və müştəriyə anında `201 Created` statusu qaytarılır. 6 rəqəmli OTP kodunun generasiyası və mailə göndərilməsi arxa planda, `@Async` annotasiyası və layihə üçün xüsusi ayrılmış **Thread Pool (Executor Service)** tərəfindən icra olunur.
* **OTP Müddəti:** OTP kodunun etibarlılıq müddəti **90 saniyədir**. Kod vaxtında təsdiqlənərsə profil `PENDING` rejimindən `ACTIVE` rejimə keçir.

### 2. Rate Limiting & Resurs Müdafiəsi (Anti-Brute-Force)
* Poçt serverinin lazımsız yerə yüklənməsinin (Mail Spamming) və daxili resursların tükənməsinin qarşısını almaq üçün təkrar kod istəyi (`resend-code`) ciddi şəkildə limitlənmişdir.
* Eyni istifadəçi ard-arda maksimum **3 dəfə** yeni OTP kodu tələb edə bilər.
* 3-cü cəhddən sonra sistem təhlükəsizlik səbəbi ilə istifadəçini **1 saatlıq müvəqqəti bloka edir**. Bu müddət ərzində gələn sorğular SMTP səviyyəsinə ötürülmədən birbaşa HTTP `429 Too Many Requests` statusu ilə dərhal geri qaytarılır və tətbiq resursları qorunur.

### 3. Aktiv Seans Limiti (FIFO) & Session Hijacking-ə Müdafiə
* **Cihaz Limiti:** Resursların idarə olunması baxımından bir istifadəçiyə eyni anda maksimum **5 fərqli cihazdan** (5 aktiv Refresh Token) giriş etməyə icazə verilir. 6-cı cihaz daxil olduqda, sistem `FIFO (First-In, First-Out)` məntiqi ilə ən köhnə seansı bazadan avtomatik təmizləyir.
* **Sessiya Oğurluğu Müdafiəsi:** `/refresh-token` endpointinə sorğu daxil olduqda, sistem yalnız tokenin tarixinə baxmır; eyni zamanda sorğunu göndərən cihazın metadatalarını (`User-Agent`) bazadakı ilkin qeydlərlə yoxlamadan keçirir. Əgər tokenin başqa brauzer və ya cihaz tərəfindən oğurlandığı (məlumat uyğunsuzluğu) təsbit edilərsə, sistem dərhal fövqəladə vəziyyət elan edir: **Həmin istifadəçiyə aid bütün cihazların daxili bazadakı bütün refresh tokenləri silinir (Global Logout)** və potensial hücum edənlə bərabər real istifadəçi də daxil olmaqla hər kəs sistemdən kənarlaşdırılır.

### 4. Optimize Edilmiş Verilənlər Bazası (Scheduled Automated Cleanup)
* Vaxtı keçmiş (expired) refresh tokenlərin verilənlər bazasında yığılıb qalması zamanla SQL sorğularının (Index Scan) sürətinə mənfi təsir edir.
* Layihəyə inteqrasiya etdiyim `RefreshTokenCleanTask` sinfi Spring-in `@Scheduled` mexanizmindən istifadə edərək müəyyən olunmuş cron ifadəsi ilə (məsələn, hər gecə saat 03:00-da) arxa planda avtomatik işə düşür.
* Baza səviyyəsində performans itkisinin qarşısını almaq üçün silinmə sorğusu custom `@Modifying` və `@Query` annotasiyaları ilə optimizasiya edilib və bütün köhnəlmiş məlumatlar tək bir tranzaksiya daxilində toplu şəkildə (**Bulk Delete**) təmizlənir.

---

## Texnoloji Stack

* **Core Framework:** Java 17 / Spring Boot 3.x
* **Token Infrastructure:** JWT (JSON Web Tokens) with Token Rotation
* **Database & ORM:** PostgreSQL / Hibernate
* **Containerization:** Docker / Docker Compose (Multi-Container DevOps Pipeline)
* **Task Automation:** Spring Async & Spring Scheduling
* **Documentation:** OpenAPI 3 / Swagger UI
* **Build System & Tools:** Maven / Project Lombok

---

## 📂 Layihə Strukturu (Architecture Layers)

Sistem daxili asılılıqların minimuma endirilməsi və **Clean Code** prinsipləri əsasında **3-Tier (Təbəqəli) Memarlıq** modelinə uyğun olaraq aşağıdakı struktura bölünüb:

```text
src/main/java/com/core/identity
│
├── 📁 config        --> Tətbiq səviyyəli konfiqurasiyalar, Thread Pool (Executor) və asinxron iş mexanizmləri
├── 📁 controller    --> API giriş qapısı, HTTP statusların idarə olunması və DTO validasiyaları
├── 📁 dto           --> Təbəqələr arası təhlükəsiz məlumat ötürülməsi üçün Request/Response modelləri
├── 📁 enums         --> Sistem daxili sabit dəyərlər (Məsələn: İstifadəçi rolları)
├── 📁 exception     --> Qlobal xəta mexanizmi (Global Exception Handler) və custom biznes xətaları
├── 📁 model         --> JPA Entity sinifləri və verilənlər bazası şeması
├── 📁 repository    --> Spring Data JPA interfeysləri və optimizasiya edilmiş custom SQL/JPQL sorğuları
├── 📁 security      --> JWT Filtrləri, Custom Authentication Provider, token generatörləri və CORS ayarlar
├── 📁 service       --> Biznes məntiqinin icra olunduğu, tranzaksiyaların idarə edildiyi əsas təbəqə
├── 📁 task          --> Arxa planda işləyən avtomatlaşdırılmış cron tapşırıqları (Cleanup token mexanizmləri)
├── 📁 util          --> Köməkçi sinif və methodlar
└── 📄 IdentityApplication.java --> (Main) sinif
```

## API Documentation

Layihə API endpoint-lərinin test edilməsi və yoxlanılması üçün Swagger UI dəstəyi ilə gəlir.

Tətbiq işə düşdükdən sonra aşağıdakı url vasitəsilə Swagger interfeysinə daxil ola bilərsiniz:

```
http://localhost:8080/swagger-ui/index.html
```

## Quraşdırma

   ```bash
   git clone https://github.com/rasuljangirli/spring-boot-advanced-auth.git
   ```

   ### Environment Variables

   Layihəni işə salmazdan əvvəl əsas qovluqda `.env` faylı yaradın.
   
   Lazımi dəyişənlərin nümunəsi layihə daxilində olan `.env.example` faylında təqdim olunub.
   
   `.env.example` faylını kopyalayaraq `.env` faylı yaradın və öz məlumatlarınızla doldurun:

Ardından isə digər əmrlər icra edilə bilər

   ```bash
   cd spring-boot-advanced-auth
   ```
   ```bash
   docker compose up --build -d
   ```

---

Youtube kanalımda dərs videolarından yararlanmaq üçün [klikləyin](https://www.youtube.com/@rasul_jangirli)

Medium hesabımda məqalələrimlə tanış olmaq üçün [klikləyin](https://medium.com/@rasuljangirli)
