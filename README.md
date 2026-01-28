# 🧩 Flexible Plugin-based Architecture with sbp

본 프로젝트는 엔터프라이즈 솔루션 납품 시 고질적으로 발생하는 **"고객사별 커스텀 코드 파편화(Branch Fragmentation)"** 문제를 해결하기 위한 아키텍처 모델입니다. 핵심 코어를 수정하지 않고 기능을 독립적으로 확장하며, 런타임에 기능을 제어(Feature Toggle)하는 것을 목표로 합니다.

---

## 1. 아키텍처 철학 (Philosophy)

본 구조의 핵심은 **"격리와 제어"**입니다. 비즈니스의 공통 분모는 코어(Core)가 단단하게 유지하고, 가변적인 요구사항은 플러그인(Plugin) 영역으로 밀어내어 유지보수 효율을 극대화합니다.

### Single Source of Truth
모든 고객사 환경에서 동일한 코어 바이너리를 사용합니다. 버전 관리와 보안 패치 대응이 단순해집니다.

### On-Demand Extension
기능을 JAR 단위로 독립 빌드하여 배포합니다. 관리자 설정만으로 즉시 활성화/비활성화가 가능합니다.

### Zero Contamination
커스텀 코드가 코어 소스에 섞이지 않습니다. 코어 업데이트 시 사이드 이펙트(Side Effect)를 원천 차단합니다.

---

## 2. 주요 메커니즘 (Key Mechanisms)

### 📥 UI Slot & Fragment Injection
코어 레이아웃의 특정 지점에 '슬롯(Slot)'을 배치합니다. 플러그인은 이 슬롯에 자신의 Thymeleaf Fragment를 주입하여 화면을 구성합니다.

**Case:** 기존 화면 수정 없이 상단 툴바에 '고객사 전용 엑셀 다운로드' 버튼 동적 추가.

### 🔗 Dynamic Menu & Router Mapping
sbp 라이브러리를 통해 각 플러그인의 컨트롤러와 엔드포인트를 코어의 네비게이션 엔진에 자동으로 등록합니다.

**Case:** 플러그인 로드 시 사이드바에 '전용 관리 메뉴' 자동 생성 및 권한 연동.

### ⚙️ Data Interception (Hooking)
비즈니스 로직 중간에 인터셉터(Interceptor)를 두어, 코어 수정 없이 데이터를 확장하거나 가공합니다.

- **Fetch:** 코어의 표준 데이터셋 로드 (예: 접속 이력 기본 정보)
- **Hook:** 플러그인이 특정 필드(예: 고객사 전용 사번, 부서명)를 런타임 주입
- **Render:** 가공된 데이터를 바탕으로 jqGrid 등 UI 컴포넌트에 동적 컬럼 노출

---

## 3. 운영 및 기술 스택 (Tech Stack)

### Deployment & Operation

#### Hot Deployment
빌드된 JAR 파일을 서버의 `/plugins` 디렉토리에 복사하는 것만으로 설치가 완료됩니다.

#### Feature Toggle
관리자 화면에서 플러그인 상태를 실시간 제어하여 엔지니어의 현장 대응력을 높입니다.

### Technical Stack

| 구분 | 기술 스택 |
|------|-----------|
| **Framework** | Spring Boot 3.x / Java 21 |
| **Plugin Engine** | sbp (Spring Boot Plugins) - PF4J 기반의 컨텍스트 격리 엔진 |
| **View** | Thymeleaf (Fragment-based Composition) |
| **Frontend** | jQuery / jqGrid (Legacy Support) |

---

## 4. 프로젝트 관리 원칙 (Git Strategy)

### Core Repository
공통 표준 기능 및 플러그인 인터페이스(SPI) 정의에 집중합니다. 고객사별 브랜치 생성을 지양합니다.

### Plugin Repository
각 플러그인은 독립된 저장소에서 관리합니다. 코어의 SPI를 상속받아 구현하며, 코어의 릴리즈 주기와 무관하게 독립적인 배포 사이클을 가집니다.

---

## 📦 프로젝트 구조

```
spring-template/
├── core/                           # 코어 애플리케이션
│   ├── src/main/java/
│   │   └── com/project/core/
│   │       ├── CoreApplication.java
│   │       ├── config/            # 설정
│   │       ├── controller/        # 컨트롤러
│   │       ├── dto/               # DTO
│   │       └── extension/         # 플러그인 확장 포인트
│   └── plugins/                   # 플러그인 JAR 배포 디렉토리
└── plugins/                       # 플러그인 소스 코드
    ├── kcredit/                   # Kcredit 플러그인
    └── hyundai/                   # Hyundai 플러그인
```

---

## 🚀 시작하기

### 빌드

```bash
mvn clean install
```

### 실행

```bash
cd core
mvn spring-boot:run
```

### 플러그인 배포

```bash
# 플러그인 빌드
cd plugins/kcredit
mvn clean package

# JAR 파일을 코어의 plugins 디렉토리로 복사
cp target/kcredit-*.jar ../../core/plugins/
```

---

## 📝 License

이 프로젝트는 엔터프라이즈 솔루션 아키텍처 참고용 모델입니다.