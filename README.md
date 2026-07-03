# Lookies App 🎨

An Android marketplace platform connecting **artists**, **partners (galleries/merchants)**, and **buyers** through curated art exhibitions and events. Lookies App enables artists to showcase and sell their paintings, partners to organize and host art events, and buyers to discover, purchase, and collect artwork — all in one place.

---

## ✨ Features

### 👤 Buyers
- Browse and search for paintings and art events
- Purchase artwork via online checkout (QRIS & Virtual Account)
- Track orders, transactions, and shipments
- Submit refund requests
- Manage delivery addresses
- In-app notifications and forum participation

### 🖌️ Artists
- Apply for artist status (submission & verification)
- Upload, edit, and manage personal paintings
- Register paintings for events and exhibitions
- View sales dashboard and financial history
- Track exhibition shipments and manage unsold artwork returns

### 🏛️ Partners (Galleries / Merchants)
- Apply for partner/merchant status
- Create and manage art events & exhibitions
- Manage event paintings and participant lists
- Process offline checkouts with QR/barcode scanning
- Handle withdrawal requests and monthly financial reports
- Invite and manage merchant members
- Chat with buyers and artists

### 🛡️ Admins
- Oversee all transactions and events
- Review and manage withdrawal requests
- Moderate platform content

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI Framework | Jetpack Compose + Material 3 |
| Architecture | Clean Architecture (Data / Domain / Presentation) |
| DI | Hilt |
| Backend | Supabase (Auth, Database, Storage, Realtime) |
| Payments | Xendit (QRIS & Virtual Account) |
| Push Notifications | Firebase Cloud Messaging (FCM) |
| Local Storage | Room (Database) + DataStore (Preferences) |
| Networking | Ktor Client + OkHttp |
| Image Loading | Coil 3 |
| Barcode / QR | ZXing + ML Kit Barcode Scanning + CameraX |
| Background Tasks | WorkManager |
| Navigation | Jetpack Navigation Compose |
| Serialization | Kotlinx Serialization |

---

## 🏗️ Project Architecture

The project follows **Clean Architecture** organized into these layers:

```
app/src/main/java/com/prayatna/lookiesapp/
├── data/
│   ├── local/
│   │   ├── datastore/         # Preferences storage
│   │   └── room/              # Local database (DAOs, Entities)
│   ├── mapper/                # DTO ↔ Domain model mappers
│   ├── remote/
│   │   ├── api/
│   │   │   ├── firebase/      # FCM service
│   │   │   ├── supabase/      # Supabase API clients
│   │   │   └── xendit/        # Xendit payment API
│   │   └── dto/               # Request / Response DTOs
│   └── repository/            # Repository implementations
├── domain/
│   ├── model/                 # Domain entities
│   ├── repository/            # Repository interfaces
│   └── usecase/               # Use cases (admin, artist, auth, event, ...)
├── presentation/              # UI screens & ViewModels
│   ├── admin/                 # Admin screens
│   ├── artistDashboard/       # Artist dashboard
│   ├── chat/ & forum/         # Messaging & community
│   ├── checkout/              # Checkout flow
│   ├── event/ & eventPainting/# Event discovery & details
│   ├── payment/               # QRIS & VA payment screens
│   ├── scanner/               # QR / barcode scanner
│   ├── shipment/              # Shipment tracking
│   ├── transaction/           # Transaction history
│   ├── user/                  # Profile, artist/partner submission
│   └── ...
├── ui/theme/                  # Colors, typography, theme
├── utils/                     # Helpers, constants, routes
└── worker/                    # FCM token WorkManager tasks
```

---

## ⚙️ Setup & Configuration

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 17
- Android SDK (compileSdk 36, minSdk 24)

### 1. Clone the repository
```bash
git clone https://github.com/prayatnaaa/lookies-app.git
cd lookies-app
```

### 2. Configure `local.properties`
Create or update your `local.properties` file in the root of the project with your credentials:

```properties
# Supabase
BASE_URL=https://<your-project>.supabase.co
API_KEY=<your-supabase-anon-key>
SUPABASE_EDGE_BASE_URL=https://<your-project>.supabase.co/functions/v1

# Xendit
XENDIT_SECRET_KEY=<your-xendit-secret-key>
```

> ⚠️ **Never commit `local.properties` to version control.** It is already listed in `.gitignore`.

### 3. Add Firebase configuration
Place your `google-services.json` file into `app/`:
```
app/google-services.json
```

### 4. Build and run
Open the project in Android Studio and run it on a device or emulator with API 24+.

---

## 🔐 Permissions

| Permission | Purpose |
|---|---|
| `INTERNET` | Network requests to Supabase, Xendit, and Firebase |
| `POST_NOTIFICATIONS` | Push notifications via FCM |
| `CAMERA` | QR code & barcode scanning for offline checkout |

---

## 💳 Payment Flow

Lookies App integrates **Xendit** for payment processing:
- **QRIS** — Scan-to-pay via QR code
- **Virtual Account (VA)** — Bank transfer via generated VA number

Payment deep-link callback scheme: `lookiesapp://payment`

---

## 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest
```

Instrumented tests use a custom `HiltTestRunner` for dependency injection and `WorkManagerTestInitHelper` for background task testing.

---

## 📦 Build

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease
```

---

## 📄 License

This project is private and proprietary. All rights reserved.
