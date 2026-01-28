🧩 Flexible Plugin-based Architecture
이 프로젝트는 솔루션 납품 시 발생하는 "고객사별 커스텀 코드 파편화" 문제를 해결하기 위한 아키텍처 제안입니다. 핵심 소스를 건드리지 않고 기능을 독립적으로 확장하며, 운영 환경에서 즉시 제어(On/Off)하는 것을 목표로 합니다.

1. 아키텍처 철학 (Philosophy)
   본 구조의 핵심은 **"격리와 제어"**입니다. 공통 비즈니스 로직은 코어(Core)가 단단하게 유지하고, 가변적인 요구사항은 플러그인(Plugin)으로 밀어내어 유지보수 효율을 높였습니다.

Single Source of Truth: 모든 고객사 환경에서 동일한 코어 바이너리를 사용합니다. 버전 관리가 단순해집니다.

On-Demand Extension: 기능을 JAR 단위로 꽂고, 서버 재기동 없이(또는 관리자 설정만으로) 즉시 활성화합니다.

Zero Contamination: 커스텀 코드가 코어 소스에 섞이지 않습니다. 덕분에 코어의 보안 패치나 기능 업데이트 시 사이드 이펙트가 거의 없습니다.

2. 주요 메커니즘 (Key Mechanisms)
   📥 UI Slot & Fragment Injection
   코어 레이아웃 곳곳에 '슬롯'을 배치합니다. 활성화된 플러그인은 이 슬롯에 자신의 Thymeleaf Fragment를 끼워 넣습니다.

예시: 기존 화면 수정 없이 상단 툴바에 '고객사 전용 엑셀 다운로드' 버튼을 동적으로 추가할 수 있습니다.

🔗 Dynamic Menu Routing
코어 네비게이션 엔진은 플러그인이 제공하는 메뉴 메타데이터를 런타임에 읽어옵니다.

예시: 플러그인을 꽂는 것만으로 전용 대시보드나 관리 메뉴가 사이드바에 자동으로 생성됩니다.

⚙️ Data Interception (Hooking)
DB 조회나 로직 처리 과정에 인터셉터를 두어, 코어 로직을 직접 수정하지 않고 데이터 구조를 확장합니다.

Fetch: 코어의 표준 데이터셋 로드

Hook: 플러그인이 특정 필드(사번, 부서 등)를 런타임에 주입

Render: 가공된 데이터를 바탕으로 jqGrid 등에 동적 컬럼 노출

3. 운영 및 기술 스택 (Operation & Tech)
   배포: 빌드된 JAR 파일을 /plugins 폴더에 넣는 것만으로 설치가 완료됩니다.

Feature Toggle: 관리자 화면에서 각 플러그인 기능을 실시간으로 켜고 끌 수 있어, 엔지니어의 운영 대응이 빨라집니다.

Stack:

Framework: Spring Boot 3.x / Java 21

Plugin Engine: sbp (Spring Boot Plugins) - PF4J 기반의 Spring 최적화 엔진

View: Thymeleaf (Fragment-based Composition)

Frontend: jQuery/jqGrid (Legacy Support)

4. 프로젝트 관리 원칙 (Git Strategy)
   Core Repo: 공통 로직만 관리합니다. 고객사별로 브랜치를 따는 행위를 지양합니다.

Plugin Repo: 각 플러그인은 독립 저장소에서 관리하며, 코어가 제공하는 인터페이스(SPI)만 상속받아 구현합니다.