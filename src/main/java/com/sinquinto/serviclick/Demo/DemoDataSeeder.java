package com.sinquinto.serviclick.Demo;

import com.sinquinto.serviclick.Appointment.Domain.AppointmentStatus;
import com.sinquinto.serviclick.Appointment.Infrastructure.Entity.AppointmentEntity;
import com.sinquinto.serviclick.Appointment.Infrastructure.Repository.AppointmentJpaRepository;
import com.sinquinto.serviclick.Rating.Infrastructure.Entity.RatingEntity;
import com.sinquinto.serviclick.Rating.Infrastructure.Repository.RatingJpaRepository;
import com.sinquinto.serviclick.ServiceOffer.Infrastructure.Entity.ServiceOfferEntity;
import com.sinquinto.serviclick.ServiceOffer.Infrastructure.Repository.ServiceOfferJpaRepository;
import com.sinquinto.serviclick.User.Domain.Role;
import com.sinquinto.serviclick.User.Infrastructure.Entity.UserEntity;
import com.sinquinto.serviclick.User.Infrastructure.Repository.SpringUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Carga datos de demostración al iniciar el backend.
 * Solo se activa cuando app.seed-demo-data=true en application.properties.
 * Es idempotente: no duplica usuarios, servicios, citas ni reseñas existentes.
 *
 * Cuentas demo genéricas (password: Demo1234):
 *   cliente.demo@serviclick.com
 *   vendedor1@serviclick.com  (Carlos Martínez  — Tecnología)
 *   vendedor2@serviclick.com  (Laura Gómez      — Diseño)
 *   vendedor3@serviclick.com  (Andrés Pérez     — Clases)
 *   vendedor4@serviclick.com  (Natalia Rodríguez — Hogar)
 *   vendedor5@serviclick.com  (Miguel Torres    — Hogar)
 *
 * Cuentas personalizadas:
 *   estefanyardilatv@gmail.com / luckychar12  (Estefany Ardila — Legal)
 *   sarahysalcedo12@gmail.com  / sarahy12     (Sarahy Salcedo  — CLIENTE)
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "app.seed-demo-data", havingValue = "true")
@RequiredArgsConstructor
public class DemoDataSeeder implements CommandLineRunner {

    private final SpringUserRepository       userRepo;
    private final ServiceOfferJpaRepository  serviceOfferRepo;
    private final AppointmentJpaRepository   appointmentRepo;
    private final RatingJpaRepository        ratingRepo;
    private final PasswordEncoder            passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("[DEMO] ======= Iniciando carga de datos demo =======");

        // ── 1. USUARIOS ──────────────────────────────────────────────────────────
        UserEntity cliente  = upsertUser("Cliente",   "Demo",       "cliente.demo@serviclick.com",  "Demo1234",    Role.CUSTOMER,    "3001112233");
        UserEntity carlos   = upsertUser("Carlos",    "Martínez",   "vendedor1@serviclick.com",     "Demo1234",    Role.SALESPERSON, "3002223344");
        UserEntity laura    = upsertUser("Laura",     "Gómez",      "vendedor2@serviclick.com",     "Demo1234",    Role.SALESPERSON, "3003334455");
        UserEntity andres   = upsertUser("Andrés",    "Pérez",      "vendedor3@serviclick.com",     "Demo1234",    Role.SALESPERSON, "3004445566");
        UserEntity natalia  = upsertUser("Natalia",   "Rodríguez",  "vendedor4@serviclick.com",     "Demo1234",    Role.SALESPERSON, "3005556677");
        UserEntity miguel   = upsertUser("Miguel",    "Torres",     "vendedor5@serviclick.com",     "Demo1234",    Role.SALESPERSON, "3006667788");

        // Cuentas personalizadas
        UserEntity estefany = upsertUser("Estefany",  "Ardila",     "estefanyardilatv@gmail.com",   "luckychar12", Role.SALESPERSON, "3007778899");
        UserEntity sarahy   = upsertUser("Sarahy",    "Salcedo",    "sarahysalcedo12@gmail.com",    "sarahy12",    Role.CUSTOMER,    "3008889900");

        // ── 2. SERVICIOS ─────────────────────────────────────────────────────────
        // Carlos — Tecnología
        ServiceOfferEntity s1  = upsertService(carlos.getUserId(), "Reparación de computadores",
                "Diagnóstico y reparación de equipos de cómputo con garantía de servicio.",
                new BigDecimal("80000"),  120, "Tecnología");
        ServiceOfferEntity s2  = upsertService(carlos.getUserId(), "Instalación de software",
                "Instalación y configuración de programas según tus necesidades.",
                new BigDecimal("50000"),   60, "Tecnología");
        ServiceOfferEntity s3  = upsertService(carlos.getUserId(), "Mantenimiento preventivo PC",
                "Limpieza interna, actualización de drivers y optimización del equipo.",
                new BigDecimal("60000"),   90, "Tecnología");

        // Laura — Diseño
        ServiceOfferEntity s4  = upsertService(laura.getUserId(), "Diseño de logos",
                "Creación de logotipo profesional con entrega en formatos editables.",
                new BigDecimal("200000"), 240, "Diseño");
        ServiceOfferEntity s5  = upsertService(laura.getUserId(), "Diseño de piezas publicitarias",
                "Banners, flyers y piezas digitales para redes sociales.",
                new BigDecimal("150000"), 180, "Diseño");
        ServiceOfferEntity s6  = upsertService(laura.getUserId(), "Identidad visual para negocios",
                "Branding completo: logo, paleta de colores, tipografía y manual de marca.",
                new BigDecimal("500000"), 480, "Diseño");

        // Andrés — Clases
        ServiceOfferEntity s7  = upsertService(andres.getUserId(), "Clases de matemáticas",
                "Refuerzo escolar y universitario en álgebra, cálculo y estadística.",
                new BigDecimal("60000"),   60, "Clases");
        ServiceOfferEntity s8  = upsertService(andres.getUserId(), "Tutorías de programación",
                "Aprende Java, Python o desarrollo web desde cero con proyectos reales.",
                new BigDecimal("80000"),   90, "Clases");
        ServiceOfferEntity s9  = upsertService(andres.getUserId(), "Asesoría en trabajos universitarios",
                "Orientación metodológica para tesis, proyectos y trabajos de grado.",
                new BigDecimal("70000"),   60, "Clases");

        // Natalia — Hogar
        ServiceOfferEntity s10 = upsertService(natalia.getUserId(), "Limpieza de apartamentos",
                "Limpieza completa del hogar incluyendo cocina, baños y habitaciones.",
                new BigDecimal("120000"), 180, "Hogar");
        ServiceOfferEntity s11 = upsertService(natalia.getUserId(), "Organización de espacios",
                "Reorganización y decoración funcional de ambientes del hogar.",
                new BigDecimal("100000"), 120, "Hogar");
        ServiceOfferEntity s12 = upsertService(natalia.getUserId(), "Limpieza profunda por horas",
                "Servicio de limpieza intensiva cobrado por hora.",
                new BigDecimal("50000"),   60, "Hogar");

        // Miguel — Hogar
        ServiceOfferEntity s13 = upsertService(miguel.getUserId(), "Instalación eléctrica básica",
                "Instalación de tomacorrientes, interruptores y puntos de luz.",
                new BigDecimal("150000"), 120, "Hogar");
        ServiceOfferEntity s14 = upsertService(miguel.getUserId(), "Reparación de tomacorrientes",
                "Diagnóstico y reparación de fallas eléctricas en el hogar.",
                new BigDecimal("80000"),   60, "Hogar");
        ServiceOfferEntity s15 = upsertService(miguel.getUserId(), "Mantenimiento de iluminación",
                "Cambio de bombillos, instalación de lámparas y mantenimiento de luminarias.",
                new BigDecimal("70000"),   90, "Hogar");

        // Estefany — Legal  (flujo completo de demo con Sarahy)
        ServiceOfferEntity se1 = upsertService(estefany.getUserId(), "Asesoría jurídica básica",
                "Consulta legal personalizada para resolver dudas legales cotidianas.",
                new BigDecimal("150000"),  60, "Legal");
        ServiceOfferEntity se2 = upsertService(estefany.getUserId(), "Revisión de contratos",
                "Análisis y revisión detallada de contratos civiles y comerciales.",
                new BigDecimal("200000"),  90, "Legal");
        ServiceOfferEntity se3 = upsertService(estefany.getUserId(), "Consultoría empresarial",
                "Asesoría legal para constitución, contratos y cumplimiento empresarial.",
                new BigDecimal("180000"),  60, "Legal");
        ServiceOfferEntity se4 = upsertService(estefany.getUserId(), "Asesoría laboral",
                "Orientación sobre derechos laborales, contratos y despidos.",
                new BigDecimal("160000"),  60, "Legal");
        // Servicio extra: permite mostrar "Añadir reseña" en vivo durante la demo
        ServiceOfferEntity se5 = upsertService(estefany.getUserId(), "Redacción de documentos legales",
                "Elaboración de cartas, poderes, derechos de petición y documentos jurídicos.",
                new BigDecimal("120000"),  45, "Legal");

        // ── 3. SOLICITUDES (APPOINTMENTS) ────────────────────────────────────────
        LocalDateTime now = LocalDateTime.now();

        // ── Cliente genérico (cliente.demo) ──────────────────────────────────────
        AppointmentEntity apt1 = upsertAppointment(cliente.getUserId(), s1.getServiceOfferId(),
                now.plusDays(7),   AppointmentStatus.PENDING);   // PENDING → Carlos
        AppointmentEntity apt2 = upsertAppointment(cliente.getUserId(), s4.getServiceOfferId(),
                now.plusDays(14),  AppointmentStatus.ACCEPTED);  // ACCEPTED → Laura
        AppointmentEntity apt3 = upsertAppointment(cliente.getUserId(), s8.getServiceOfferId(),
                now.minusDays(15), AppointmentStatus.COMPLETED); // COMPLETED → Andrés (reseña)
        AppointmentEntity apt4 = upsertAppointment(cliente.getUserId(), s10.getServiceOfferId(),
                now.minusDays(10), AppointmentStatus.COMPLETED); // COMPLETED → Natalia (reseña)
        AppointmentEntity apt5 = upsertAppointment(cliente.getUserId(), s14.getServiceOfferId(),
                now.minusDays(3),  AppointmentStatus.REJECTED);  // REJECTED → Miguel

        // ── Sarahy ↔ Estefany (flujo completo para la exposición) ────────────────
        AppointmentEntity aptS1 = upsertAppointment(sarahy.getUserId(), se1.getServiceOfferId(),
                now.plusDays(5),   AppointmentStatus.PENDING);   // PENDING — Asesoría jurídica
        AppointmentEntity aptS2 = upsertAppointment(sarahy.getUserId(), se2.getServiceOfferId(),
                now.plusDays(12),  AppointmentStatus.ACCEPTED);  // ACCEPTED — Revisión de contratos
        AppointmentEntity aptS3 = upsertAppointment(sarahy.getUserId(), se3.getServiceOfferId(),
                now.minusDays(20), AppointmentStatus.COMPLETED); // COMPLETED — Consultoría (reseña)
        AppointmentEntity aptS4 = upsertAppointment(sarahy.getUserId(), se4.getServiceOfferId(),
                now.minusDays(5),  AppointmentStatus.REJECTED);  // REJECTED — Asesoría laboral
        // COMPLETED sin reseña → muestra botón "Añadir reseña" durante la demo
        AppointmentEntity aptS5 = upsertAppointment(sarahy.getUserId(), se5.getServiceOfferId(),
                now.minusDays(8),  AppointmentStatus.COMPLETED); // COMPLETED — sin reseña (para demo en vivo)

        // ── 4. RESEÑAS ───────────────────────────────────────────────────────────
        // Cliente genérico
        upsertRating(cliente.getUserId(), s8.getServiceOfferId(),  apt3.getAppointmentId(),
                5, "Excelente servicio, muy claro y puntual.");
        upsertRating(cliente.getUserId(), s10.getServiceOfferId(), apt4.getAppointmentId(),
                4, "Buen servicio, cumplió con lo solicitado.");

        // Sarahy → Estefany
        upsertRating(sarahy.getUserId(), se3.getServiceOfferId(), aptS3.getAppointmentId(),
                5, "Muy profesional, resolvió todas mis dudas legales. Totalmente recomendada.");

        log.info("[DEMO] ======= Datos demo cargados exitosamente =======");
        log.info("[DEMO] --- Cuentas genéricas (password: Demo1234) ---");
        log.info("[DEMO] Cliente:     cliente.demo@serviclick.com");
        log.info("[DEMO] Vendedor 1:  vendedor1@serviclick.com  (Carlos Martínez — Tecnología)");
        log.info("[DEMO] Vendedor 2:  vendedor2@serviclick.com  (Laura Gómez — Diseño)");
        log.info("[DEMO] Vendedor 3:  vendedor3@serviclick.com  (Andrés Pérez — Clases)");
        log.info("[DEMO] Vendedor 4:  vendedor4@serviclick.com  (Natalia Rodríguez — Hogar)");
        log.info("[DEMO] Vendedor 5:  vendedor5@serviclick.com  (Miguel Torres — Hogar)");
        log.info("[DEMO] --- Cuentas personalizadas ---");
        log.info("[DEMO] Vendedora:   estefanyardilatv@gmail.com / luckychar12  (Estefany Ardila — Legal)");
        log.info("[DEMO] Cliente:     sarahysalcedo12@gmail.com  / sarahy12     (Sarahy Salcedo)");
    }

    // ─── helpers ────────────────────────────────────────────────────────────────

    private UserEntity upsertUser(String name, String lastName, String email,
                                   String password, Role role, String phone) {
        UserEntity existing = userRepo.findByEmail(email);
        if (existing != null) {
            log.info("[DEMO] Usuario ya existe: {}", email);
            return existing;
        }
        UserEntity saved = userRepo.save(UserEntity.builder()
                .name(name).lastName(lastName).email(email)
                .password(passwordEncoder.encode(password))
                .phoneNumber(phone).role(role)
                .registerDate(LocalDateTime.now())
                .build());
        log.info("[DEMO] Usuario creado: {} (id={})", email, saved.getUserId());
        return saved;
    }

    private ServiceOfferEntity upsertService(Long sellerId, String title, String description,
                                              BigDecimal price, int duration, String category) {
        List<ServiceOfferEntity> existing = serviceOfferRepo.findBySellerId(sellerId);
        return existing.stream()
                .filter(s -> title.equals(s.getTitle()))
                .findFirst()
                .orElseGet(() -> {
                    ServiceOfferEntity saved = serviceOfferRepo.save(ServiceOfferEntity.builder()
                            .sellerId(sellerId).title(title).description(description)
                            .price(price).estimatedDuration(duration).category(category)
                            .photo(null).publicationDate(LocalDateTime.now())
                            .build());
                    log.info("[DEMO] Servicio creado: '{}' (id={})", title, saved.getServiceOfferId());
                    return saved;
                });
    }

    private AppointmentEntity upsertAppointment(Long userId, Long serviceOfferId,
                                                 LocalDateTime date, AppointmentStatus status) {
        return appointmentRepo.findByUserId(userId).stream()
                .filter(a -> a.getServiceOfferId().equals(serviceOfferId))
                .findFirst()
                .orElseGet(() -> {
                    AppointmentEntity saved = appointmentRepo.save(AppointmentEntity.builder()
                            .userId(userId).serviceOfferId(serviceOfferId)
                            .date(date).status(status)
                            .build());
                    log.info("[DEMO] Cita creada: id={} status={}", saved.getAppointmentId(), status);
                    return saved;
                });
    }

    private void upsertRating(Long userId, Long serviceOfferId, Long appointmentId,
                               int score, String comment) {
        if (ratingRepo.existsByAppointmentId(appointmentId)) {
            log.info("[DEMO] Reseña ya existe: appointmentId={}", appointmentId);
            return;
        }
        ratingRepo.save(RatingEntity.builder()
                .userId(userId).serviceOfferId(serviceOfferId)
                .appointmentId(appointmentId).score(score)
                .comment(comment).date(LocalDateTime.now())
                .build());
        log.info("[DEMO] Reseña creada: appointmentId={} score={}", appointmentId, score);
    }
}
