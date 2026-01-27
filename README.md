# Pragmatic Architecture Base Template

이 프로젝트는 DDD와 Hexagonal Architecture에서 필요한 것만 선택적으로 적용한 실용적 아키텍처 베이스 템플릿이다.
아키텍처 구조를 유지하기 위한 코드보다는 실제 문제 해결에 기여하는 코드만 남기는 것을 목표로 한다.

<br>

## 설계 철학

이 프로젝트는 다음 전제를 가지고 시작했다.

- 모든 추상화는 비용을 가진다
- 발생하지 않은 변화에 대비한 구조는 유지비가 된다
- 구조는 문제를 해결하기 위해 존재해야 한다

따라서 이 템플릿은 교과서적인 DDD / Hexagonal 구조를 그대로 따르지 않고 실제 변경 압력이 발생하기 전까지 추상화와 분리를 미루는 방식을 채택했다.

<br>

## 전체 패키지 구조

```text
com.example.pragmatic
├── application
├── domain
├── infrastructure
└── presentation
```

계층은 명확히 나누되 각 계층 내부에서는 과도한 추상화를 피한다.

<br>

## Domain Layer

```text
domain
├── BaseEntity.java
├── auth
│   ├── AuthenticationException.java
│   ├── InvalidCredentialsException.java
│   ├── InvalidRefreshTokenException.java
│   ├── RefreshToken.java
│   ├── RefreshTokenException.java
│   ├── RefreshTokenExpiredException.java
│   └── RefreshTokenNotFoundException.java
└── user
    ├── User.java
    ├── UserRole.java
    ├── UserStatus.java
    ├── UserException.java
    ├── UserNotFoundException.java
    └── DuplicateEmailException.java
```

### 특징

- **도메인과 엔티티를 분리하지 않는다.**
  - JPA 제약이 도메인 표현을 왜곡하지 않고 변경 압력이 낮은 범위에서는 통합
  - 불필요한 매핑 계층 제거
- **도메인 예외는 도메인 내부에 위치**
  - HTTP, 상태코드, 프레임워크 의존성 없음

<br>

## Application Layer

```text
application
├── auth
│   ├── command
│   │   ├── AuthenticationService.java
│   │   └── RefreshTokenService.java
│   ├── dto
│   │   └── LoginResult.java
│   └── facade
│       ├── LoginFacade.java
│       └── ReissueTokenFacade.java
└── user
    ├── command
    │   └── RegisterService.java
    └── query
        ├── UserFinder.java
        └── UserQueryService.java
```

### Command / Query 분리

- 쓰기(Command)와 읽기(Query)를 명확히 분리
- CQRS를 패턴이 아니라 책임 분리 수단으로 사용

### UserFinder의 역할

- UserFinder는 Application 내부에서만 사용하는 조회 계약
- 다른 도메인 서비스에서 User 조회 시 사용
- 구현은 UserQueryService가 담당

#### 목적

- 서비스 간 직접 의존으로 인한 순환 참조 방지
- Repository 직접 접근을 차단
- 조회 정책(ACTIVE 사용자만 조회 등)을 한 곳에서 관리

<br>

## Infrastructure Layer

```text
infrastructure
├── config
│   ├── JpaConfig.java
│   ├── SecurityConfig.java
│   └── properties
│       ├── CookieProperties.java
│       ├── CorsProperties.java
│       └── JwtProperties.java
├── persistence
│   ├── UserRepository.java
│   └── RefreshTokenRepository.java
└── security
    ├── audit
    │   └── AuditorAwareImpl.java
    ├── cookie
    │   └── CookieManager.java
    ├── handler
    │   ├── JwtAuthenticationEntryPoint.java
    │   └── JwtAccessDeniedHandler.java
    └── jwt
        ├── JwtAuthenticationFilter.java
        ├── JwtPrincipal.java
        └── JwtProvider.java
```

### 보안 설계 특징

- SecurityContext에는 엔티티를 넣지 않는다.
  - JwtPrincipal로 최소 정보만 유지
- JWT 파싱은 JwtProvider에서 단일 책임으로 처리
- Refresh Token은 HttpOnly Cookie로 관리
  - CookieManager가 쿠키 생성/삭제 책임을 가짐
- 인증 / 인가 실패 응답도 ProblemDetail 기반으로 통일

### Audit

- AuditorAwareImpl는 SecurityContext의 JwtPrincipal을 기반으로 동작
- 인증 정보가 없는 경우 SYSTEM 처리

<br>

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
│   │   └── RegisterRequest.java
│   └── response
│       └── UserResponse.java
└── common
    └── exception
        ├── BaseExceptionHandler.java
        ├── GlobalExceptionHandler.java
        ├── ExceptionStatusMapper.java
        └── mapper
            ├── DomainExceptionStatusMapper.java
            └── UserExceptionStatusMapper.java
```

### Controller 책임

- 요청/응답 DTO만 다룸
- 도메인 객체 -> Response 변환은 Controller 또는 Facade에서 수행
- Service 레이어는 HTTP 개념을 모름

### 예외 처리 전략

- 도메인 예외 -> 상태코드 매핑은 Presentation 계층에서 수행
- ExceptionStatusMapper + 도메인별 Mapper 구조
- 보안 예외와 일반 예외의 응답 포맷을 일관되게 유지

<br>

## 이 템플릿이 지향하는 것

- 아키텍처를 지키는 것이 아닌 선택하는 것
- 필요해질 때 추상화해도 늦지 않다는 판단
- 구조의 복잡도보다 변경 비용을 기준으로 설계

<br>

## 이 템플릿이 일부러 하지 않는 것

- 무조건적인 Port / Adapter 분리
- 도메인/엔티티 강제 분리
- 모든 서비스에 인터페이스 도입
- 미래 가능성만을 위한 선행 설계