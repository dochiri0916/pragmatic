# Pragmatic Architecture Base Template

이 프로젝트는 DDD와 Hexagonal Architecture에서 필요한 것만 선택적으로 적용한 실용적 아키텍처 베이스 템플릿이다.
아키텍처 구조를 유지하기 위한 코드보다는 실제 문제 해결에 기여하는 코드만 남기는 것을 목표로 한다.

---

## 설계 철학

이 프로젝트는 다음 전제를 가지고 시작했다.

- 모든 추상화는 비용을 가진다
- 발생하지 않은 변화에 대비한 구조는 유지비가 된다
- 구조는 문제를 해결하기 위해 존재해야 한다

따라서 이 템플릿은 교과서적인 DDD / Hexagonal 구조를 그대로 따르지 않고 실제 변경 압력이 발생하기 전까지 추상화와 분리를 미루는 방식을 채택했다.

---

## 전체 패키지 구조

```text
com.example.pragmatic
├── application
├── domain
├── infrastructure
└── presentation
```

계층은 명확히 나누되 각 계층 내부에서는 과도한 추상화를 피한다.

---

## Domain Layer

```text
domain
├── BaseEntity.java
├── auth
│   ├── AuthenticationException.java
│   ├── InvalidCredentialsException.java
│   ├── RefreshToken.java
│   ├── RefreshTokenException.java
│   ├── RefreshTokenNotFoundException.java
│   ├── InvalidRefreshTokenException.java
│   ├── ExpiredRefreshTokenException.java
│   └── RefreshTokenRepository.java
└── user
    ├── User.java
    ├── UserRole.java
    ├── UserException.java
    ├── UserNotFoundException.java
    ├── DuplicateEmailException.java
    ├── InactiveUserException.java
    └── UserRepository.java
```

### 특징

- **도메인과 엔티티를 분리하지 않는다.**
  - JPA 제약이 도메인 표현을 왜곡하지 않고 변경 압력이 낮은 범위에서는 통합
  - 불필요한 매핑 계층 제거
- **도메인 예외는 도메인 내부에 위치**
  - HTTP, 상태코드, 프레임워크 의존성 없음
  - Factory Method 패턴으로 다양한 예외 생성 시나리오 지원
- **Repository 인터페이스도 도메인 계층에 위치**
  - 도메인이 인프라에 의존하지 않도록 의존성 역전
  - 구현체는 Infrastructure 계층에 배치

---

## Application Layer

```text
application
├── auth
│   ├── command
│   │   ├── LoginCommand.java
│   │   ├── UserAuthenticationService.java
│   │   ├── IssueRefreshTokenCommand.java
│   │   ├── IssueRefreshTokenService.java
│   │   ├── RevokeTokenCommand.java
│   │   └── RevokeTokenService.java
│   ├── dto
│   │   └── LoginResult.java
│   └── facade
│       ├── LoginFacade.java
│       └── ReissueTokenFacade.java
└── user
    ├── command
    │   ├── RegisterUserCommand.java
    │   └── RegisterUserService.java
    ├── dto
    │   └── UserDetail.java
    └── query
        └── UserQueryService.java
```

### Command / Query 분리

- 쓰기(Command)와 읽기(Query)를 명확히 분리
- CQRS를 패턴이 아니라 책임 분리 수단으로 사용
- Command는 불변 객체(record)로 정의하여 데이터 무결성 보장

### Facade 계층

- 여러 Application Service를 조합하는 계층
- 트랜잭션 경계 설정
- 도메인 간 조율 역할 수행

---

## Infrastructure Layer

```text
infrastructure
├── config
│   ├── JpaConfig.java
│   ├── SchedulingConfig.java
│   ├── SecurityConfig.java
│   └── properties
│       ├── CookieProperties.java
│       ├── CorsProperties.java
│       └── JwtProperties.java
├── persistence
│   ├── user
│   │   ├── UserJpaRepository.java (Spring Data JPA)
│   │   └── JpaUserRepository.java (구현체)
│   └── refreshtoken
│       ├── RefreshTokenJpaRepository.java (Spring Data JPA)
│       └── JpaRefreshTokenRepository.java (구현체)
├── scheduler
│   └── RefreshTokenCleanupScheduler.java
└── security
    ├── audit
    │   └── AuditorAwareImpl.java
    ├── cookie
    │   └── CookieProvider.java
    ├── handler
    │   ├── JwtAuthenticationEntryPoint.java
    │   └── JwtAccessDeniedHandler.java
    └── jwt
        ├── JwtAuthenticationFilter.java
        ├── JwtPrincipal.java
        ├── JwtProvider.java
        ├── JwtTokenGenerator.java
        ├── JwtTokenResult.java
        └── RefreshTokenVerifier.java
```

### 보안 설계 특징

- **SecurityContext에는 엔티티를 넣지 않는다**
  - JwtPrincipal로 최소 정보만 유지
- **JWT 파싱은 JwtProvider에서 단일 책임으로 처리**
  - Application / Facade는 JwtProvider를 직접 사용하지 않음
  - JwtTokenGenerator, RefreshTokenVerifier를 통해 간접 접근
- **Refresh Token은 HttpOnly Cookie로 관리**
  - CookieProvider가 쿠키 생성/삭제 책임을 가짐
  - XSS 공격 방어
- **Refresh Token Rotation (RTR) 전략 적용**
  - AccessToken 재발급 시 RefreshToken도 함께 재발급
  - 기존 RefreshToken은 즉시 무효화 (rotate)
  - 토큰 탈취 위험 최소화
  - OWASP OAuth 2.0 보안 Best Practice 준수
- **userId에 unique 제약 적용**
  - 한 사용자당 하나의 RefreshToken만 유지
  - DB 레벨에서 데이터 무결성 보장
- **만료된 토큰 자동 정리**
  - 매일 03시에 스케줄러로 만료된 RefreshToken 삭제
  - DB 용량 관리 및 보안 향상
- **인증/인가 실패 응답도 ProblemDetail 기반으로 통일**
  - 일관된 에러 응답 형식

### Audit

- AuditorAwareImpl는 SecurityContext의 JwtPrincipal을 기반으로 동작
- 인증 정보가 없는 경우 SYSTEM 처리

---

## Presentation Layer

```text
presentation
├── auth
│   ├── AuthController.java
│   ├── request
│   │   └── LoginRequest.java
│   └── response
│       └── AuthResponse.java
├── user
│   ├── UserController.java
│   ├── request
│   │   └── RegisterUserRequest.java
│   └── response
│       └── UserResponse.java
└── common
    └── exception
        ├── BaseExceptionHandler.java
        ├── GlobalExceptionHandler.java
        ├── ExceptionStatusMapper.java
        └── mapper
            ├── DomainExceptionStatusMapper.java (인터페이스)
            ├── AuthenticationExceptionStatusMapper.java
            ├── RefreshTokenExceptionStatusMapper.java
            └── UserExceptionStatusMapper.java
```

### Controller 책임

- 요청/응답 DTO만 다룸
- 도메인 객체 -> Response 변환은 Response 객체의 정적 팩토리 메서드에서 수행
- Service 레이어는 HTTP 개념을 모름
- Validation은 Request DTO에서 수행

### 예외 처리 전략

- **도메인 예외 -> 상태코드 매핑은 Presentation 계층에서 수행**
  - 도메인은 HTTP를 모름 (의존성 역전)
- **Strategy 패턴 기반 Mapper 구조**
  - DomainExceptionStatusMapper 인터페이스
  - 도메인별 구현체 (AuthenticationExceptionStatusMapper, RefreshTokenExceptionStatusMapper, UserExceptionStatusMapper)
  - ExceptionStatusMapper가 List로 관리하여 확장 용이
- **일관된 에러 응답**
  - ProblemDetail(RFC 7807) 표준 준수
  - 보안 예외, 도메인 예외, Validation 예외 모두 동일한 포맷
  - timestamp, path, errors 등 상세 정보 포함
- **예외별 적절한 HTTP 상태 코드**
  - 401 UNAUTHORIZED: 인증 실패, 토큰 관련 예외
  - 403 FORBIDDEN: 권한 부족, 비활성 계정
  - 404 NOT_FOUND: 리소스 없음
  - 409 CONFLICT: 중복 리소스
  - 400 BAD_REQUEST: Validation 실패, 기타 잘못된 요청
  - 500 INTERNAL_SERVER_ERROR: 예상치 못한 서버 에러

---

## 이 템플릿이 지향하는 것

- 아키텍처를 지키는 것이 아닌 선택하는 것
- 필요해질 때 추상화해도 늦지 않다는 판단
- 구조의 복잡도보다 변경 비용을 기준으로 설계

---

## 이 템플릿이 일부러 하지 않는 것

- 무조건적인 Port / Adapter 분리
- 도메인/엔티티 강제 분리
- 모든 서비스에 인터페이스 도입
- 미래 가능성만을 위한 선행 설계