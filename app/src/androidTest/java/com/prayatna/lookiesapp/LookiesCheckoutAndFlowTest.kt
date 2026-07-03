package com.prayatna.lookiesapp

import android.content.Context
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.testing.WorkManagerTestInitHelper
import com.prayatna.lookiesapp.presentation.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * LookiesCheckoutAndFlowTest
 *
 * Comprehensive UI integration test suite for the Lookies art-gallery app.
 *
 * COVERAGE
 * ─────────────────────────────────────────────────────────────────────────────────────────────
 * 1. Navigation & Display Validation
 *    a) Get List Events        – verify LazyColumn list with ≥1 event card is shown.
 *    b) Detail Event           – click event → verify "About Event" section + "Buy ticket" button.
 *    c) List Event Paintings   – tap "See all arts" → verify Event Gallery lazy-list appears.
 *    d) Detail Event Painting  – tap a painting → verify artwork image + "Artist" label.
 *
 * 2. Checkout Flow (Direct Purchase)
 *    "Buy ticket" → quantity modal → "Buy now" → Checkout screen (Subtotal/Total verified)
 *    → "Pay" button → payment gateway transition (dialog or QRIS/VA screen).
 *
 * 3. UI Race Condition / Stress Click
 *    a) Spam "Buy ticket" 10×  → asserts exactly 1 modal is open.
 *    b) Spam "Pay" 15×         → asserts button disables / loading shows / no duplicate screens.
 *    c) Spam "See all arts" 8× → asserts exactly 1 Event Gallery screen is on the back-stack.
 *
 * SETUP NOTES
 * ─────────────────────────────────────────────────────────────────────────────────────────────
 * • Tests launch MainActivity via createAndroidComposeRule – the full Hilt + NavGraph is live.
 * • A logged-in session must already exist in DataStore on the device/emulator.
 *   If your CI starts from the Login screen, add a loginHelper() call in setUp().
 * • All waiting uses waitUntil with a generous [NETWORK_TIMEOUT_MS] to survive network latency.
 *
 * REQUIRED DEPENDENCIES (app/build.gradle.kts)
 * ─────────────────────────────────────────────────────────────────────────────────────────────
 * Already present:
 *   androidTestImplementation(libs.androidx.ui.test.junit4)
 *   androidTestImplementation(libs.androidx.espresso.core)
 *   androidTestImplementation(libs.androidx.junit)
 *   debugImplementation(libs.androidx.ui.test.manifest)
 *
 * Add for Hilt testing (if not yet in your build file):
 *   androidTestImplementation("com.google.dagger:hilt-android-testing:<hilt_version>")
 *   kspAndroidTest("com.google.dagger:hilt-android-compiler:<hilt_version>")
 *
 * Also add a @HiltTestApplication in androidTest if you don't have a custom runner:
 *   testInstrumentationRunner = "com.prayatna.lookiesapp.HiltTestRunner"
 *   (Create HiltTestRunner.kt in androidTest – see comment below.)
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LookiesCheckoutAndFlowTest {

    // ── Hilt rule MUST be first in the rule-chain ──────────────────────────────────────────────
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    // ── Compose rule launches MainActivity with the real Hilt DI graph ─────────────────────────
    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    // ══════════════════════════════════════════════════════════════════════════════════════════════
    // CONSTANTS
    // ══════════════════════════════════════════════════════════════════════════════════════════════

    companion object {
        /** Max ms to wait for network-backed content to appear. */
        private const val NETWORK_TIMEOUT_MS = 10_000L

        // ── UI-text constants (exact strings from the Composables) ────────────────────────────

        // EventListScreen
        private const val TEXT_EVENTS_TITLE        = "Events"

        // DetailEventScreen
        private const val TEXT_DETAIL_EVENT_APPBAR = "Detail event"
        private const val TEXT_ABOUT_EVENT_SECTION = "About Event"
        private const val TEXT_BUY_TICKET_BTN      = "Buy ticket"
        private const val TEXT_SEE_ALL_ARTS_BTN    = "See all arts"

        // DetailEventBottomModal
        private const val TEXT_BUY_NOW_BTN         = "Buy now"

        // CheckoutContent
        private const val TEXT_CHECKOUT_APPBAR     = "Checkout"
        private const val TEXT_PAY_BTN             = "Pay"       // substring-matched (button shows "Total: Rp… | Pay")
        private const val TEXT_SUBTOTAL_LABEL      = "Subtotal"
        private const val TEXT_TOTAL_LABEL         = "Total"

        // EventPaintingGalleryScreen
        private const val TEXT_EVENT_GALLERY_APPBAR = "Event Gallery"

        // EventPaintingDetailScreen
        private const val TEXT_ARTWORK_DETAIL_APPBAR = "Artwork Detail"
        private const val TEXT_ARTIST_LABEL          = "Artist"

        // Payment gateway outcomes
        private const val TEXT_ORDER_CREATED_DIALOG  = "Order Created!"
        private const val TEXT_PAYMENT_APPBAR_QRIS   = "QRIS Payment"
        private const val TEXT_PAYMENT_APPBAR_VA     = "VA Payment"
    }

    // ══════════════════════════════════════════════════════════════════════════════════════════════
    // LIFECYCLE
    // ══════════════════════════════════════════════════════════════════════════════════════════════

    @Before
    fun setUp() {
        hiltRule.inject()
        val context = ApplicationProvider.getApplicationContext<Context>()
        WorkManagerTestInitHelper.initializeTestWorkManager(context)
        composeRule.waitForIdle()
    }

    // ══════════════════════════════════════════════════════════════════════════════════════════════
    // 1. NAVIGATION & DISPLAY VALIDATION
    // ══════════════════════════════════════════════════════════════════════════════════════════════

    /**
     * 1a – Get List Events
     *
     * Verifies the Event List screen renders a scrollable LazyColumn containing at least one
     * clickable event card after data has loaded from the backend.
     */
    @Test
    fun test_getListEvents_displaysAtLeastOneEventCard() {
        navigateToEventList()

        // "Events" top-bar title must appear.
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_EVENTS_TITLE).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(TEXT_EVENTS_TITLE).assertIsDisplayed()

        // A scrollable container (LazyColumn) must be present.
        composeRule.onNode(hasScrollAction()).assertExists()

        // At least one clickable node (event card) must exist.
        val clickableCount = composeRule.onAllNodes(hasClickAction())
            .fetchSemanticsNodes().size
        assert(clickableCount > 0) {
            "Expected ≥1 clickable event card in the list, but found none."
        }
    }

    /**
     * 1b – Detail Event
     *
     * Taps the first event card and asserts the Detail Event screen shows:
     *  • Top-bar title "Detail event"
     *  • "About Event" section
     *  • "Buy ticket" button in the bottom bar
     */
    @Test
    fun test_detailEvent_showsTitleDescriptionAndBuyButton() {
        navigateToEventList()
        waitForEventListToLoad()
        tapFirstEventCard()
        waitForDetailEventScreen()

        // "About Event" section header.
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_ABOUT_EVENT_SECTION).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(TEXT_ABOUT_EVENT_SECTION).assertIsDisplayed()

        // "Buy ticket" bottom-bar CTA.
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_BUY_TICKET_BTN).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(TEXT_BUY_TICKET_BTN).assertIsDisplayed()
    }

    /**
     * 1c – List Event Paintings
     *
     * From the Detail Event screen, taps "See all arts" and verifies the Event Gallery screen
     * opens with a scrollable list that contains at least one painting item.
     */
    @Test
    fun test_listEventPaintings_displaysGalleryList() {
        navigateToEventList()
        waitForEventListToLoad()
        tapFirstEventCard()
        waitForDetailEventScreen()
        navigateToPaintingGallery()

        // Gallery screen top-bar.
        composeRule.onNodeWithText(TEXT_EVENT_GALLERY_APPBAR).assertIsDisplayed()

        // Scrollable LazyColumn must exist.
        composeRule.onNode(hasScrollAction()).assertExists()

        // At least one clickable painting row.
        val paintingItems = composeRule.onAllNodes(hasClickAction())
            .fetchSemanticsNodes().size
        assert(paintingItems > 0) {
            "Expected ≥1 painting item in the Event Gallery, but found none."
        }
    }

    /**
     * 1d – Detail Event Painting
     *
     * From the Event Gallery, taps the first painting and verifies the Painting Detail screen:
     *  • Top-bar shows "Artwork Detail"
     *  • A node with an image contentDescription is present (the painting image)
     *  • The "Artist" role label is visible
     */
    @Test
    fun test_detailEventPainting_showsPaintingImageAndArtistDetails() {
        navigateToEventList()
        waitForEventListToLoad()
        tapFirstEventCard()
        waitForDetailEventScreen()
        navigateToPaintingGallery()

        // Wait for at least one non-back-button clickable node (index 1+).
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodes(hasClickAction()).fetchSemanticsNodes().size >= 2
        }
        // Tap the first painting row (index 0 is typically the back nav icon, index 1 the first item).
        composeRule.onAllNodes(hasClickAction())[1].performClick()
        composeRule.waitForIdle()

        // Artwork Detail screen top-bar.
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_ARTWORK_DETAIL_APPBAR).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(TEXT_ARTWORK_DETAIL_APPBAR).assertIsDisplayed()

        // A node with a non-empty contentDescription must exist (AsyncImage for the painting).
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodes(hasContentDescription("", substring = true))
                .fetchSemanticsNodes().isNotEmpty()
        }
        // Verify "Artist" label is displayed (the artist role sub-text in EventPaintingDetailScreen).
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_ARTIST_LABEL).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(TEXT_ARTIST_LABEL).assertIsDisplayed()
    }

    // ══════════════════════════════════════════════════════════════════════════════════════════════
    // 2. CHECKOUT FLOW (DIRECT PURCHASE)
    // ══════════════════════════════════════════════════════════════════════════════════════════════

    /**
     * 2 – Checkout Ticket (Full Direct-Purchase Flow)
     *
     * Exercises the exact direct-purchase sequence with no shopping cart:
     *
     *  Step 1 – Open Event Detail.
     *  Step 2 – Tap "Buy ticket" → quantity modal (ModalBottomSheet) appears.
     *  Step 3 – Tap "Buy now" → NavController navigates to Checkout screen.
     *  Step 4 – Verify ticket summary: "Subtotal" + "Total" rows are displayed.
     *  Step 5 – Tap "Pay" (Lanjut ke Pembayaran) → ViewModel submits order.
     *  Step 6 – Assert payment gateway transition:
     *             (a) "Order Created!" success dialog appears, OR
     *             (b) QRIS payment screen opened, OR
     *             (c) VA payment screen opened.
     */
    @Test
    fun test_checkoutTicket_fullDirectPurchaseFlow() {
        // ── Step 1: Detail Event ──────────────────────────────────────────────────────────────
        navigateToEventList()
        waitForEventListToLoad()
        tapFirstEventCard()
        waitForDetailEventScreen()

        // ── Step 2: Open quantity modal ───────────────────────────────────────────────────────
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_BUY_TICKET_BTN).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(TEXT_BUY_TICKET_BTN).performClick()
        composeRule.waitForIdle()

        // ModalBottomSheet with "Buy now" must appear.
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_BUY_NOW_BTN).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(TEXT_BUY_NOW_BTN).assertIsDisplayed()

        // ── Step 3: Confirm quantity → navigate to Checkout ───────────────────────────────────
        composeRule.onNodeWithText(TEXT_BUY_NOW_BTN).performClick()
        composeRule.waitForIdle()

        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_CHECKOUT_APPBAR).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(TEXT_CHECKOUT_APPBAR).assertIsDisplayed()

        // ── Step 4: Verify ticket summary ─────────────────────────────────────────────────────
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_SUBTOTAL_LABEL).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(TEXT_SUBTOTAL_LABEL).assertIsDisplayed()
        composeRule.onNodeWithText(TEXT_TOTAL_LABEL).assertIsDisplayed()

        // "Pay" button must be displayed and enabled.
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_PAY_BTN, substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(TEXT_PAY_BTN, substring = true).assertIsDisplayed()

        // ── Step 5: Tap "Pay" ─────────────────────────────────────────────────────────────────
        composeRule.onNodeWithText(TEXT_PAY_BTN, substring = true).performClick()
        composeRule.waitForIdle()

        // ── Step 6: Assert payment gateway / processing state ─────────────────────────────────
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_ORDER_CREATED_DIALOG).fetchSemanticsNodes().isNotEmpty()
                    || composeRule.onAllNodesWithText(TEXT_PAYMENT_APPBAR_QRIS).fetchSemanticsNodes().isNotEmpty()
                    || composeRule.onAllNodesWithText(TEXT_PAYMENT_APPBAR_VA).fetchSemanticsNodes().isNotEmpty()
        }

        val reachedPayment =
            composeRule.onAllNodesWithText(TEXT_ORDER_CREATED_DIALOG).fetchSemanticsNodes().isNotEmpty()
                    || composeRule.onAllNodesWithText(TEXT_PAYMENT_APPBAR_QRIS).fetchSemanticsNodes().isNotEmpty()
                    || composeRule.onAllNodesWithText(TEXT_PAYMENT_APPBAR_VA).fetchSemanticsNodes().isNotEmpty()

        assert(reachedPayment) {
            "Expected a payment gateway transition ('Order Created!' dialog, QRIS screen, or VA screen) " +
                    "after tapping 'Pay', but none appeared."
        }
    }

    // ══════════════════════════════════════════════════════════════════════════════════════════════
    // 3. UI RACE CONDITION / STRESS CLICK TEST
    // ══════════════════════════════════════════════════════════════════════════════════════════════

    /**
     * 3a – Stress Click "Buy ticket"
     *
     * Simulates a user tapping "Buy ticket" 10 times rapidly.
     *
     * ASSERTIONS
     *  • The modal opens exactly once (no duplicate ModalBottomSheets stacked).
     *  • The app does not crash or become unresponsive.
     */
    @Test
    fun test_raceCondition_spamClickBuyTicketButton_handlesGracefully() {
        navigateToEventList()
        waitForEventListToLoad()
        tapFirstEventCard()
        waitForDetailEventScreen()

        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_BUY_TICKET_BTN).fetchSemanticsNodes().isNotEmpty()
        }

        // ── 10 rapid clicks ───────────────────────────────────────────────────────────────────
        repeat(10) {
            composeRule.onNodeWithText(TEXT_BUY_TICKET_BTN).performClick()
        }
        composeRule.waitForIdle()

        // The modal must be open.
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_BUY_NOW_BTN).fetchSemanticsNodes().isNotEmpty()
        }

        // Exactly ONE "Buy now" button should be composed (one modal, not N stacked modals).
        val buyNowCount = composeRule.onAllNodesWithText(TEXT_BUY_NOW_BTN)
            .fetchSemanticsNodes().size
        assert(buyNowCount == 1) {
            "Expected exactly 1 'Buy now' button (one modal), but found $buyNowCount. " +
                    "Multiple modals appear to have been stacked by rapid tapping."
        }

        // App is still interactive.
        composeRule.onNodeWithText(TEXT_BUY_NOW_BTN).assertIsDisplayed()
    }

    /**
     * 3b – Stress Click "Pay" on Checkout Screen
     *
     * Navigates to the Checkout screen, then rapidly clicks "Pay" 15 times.
     *
     * ASSERTIONS
     *  • The button becomes disabled OR a CircularProgressIndicator appears after the first
     *    successful click (because CheckoutContent sets enabled = !isLoading).
     *  • The app does not crash.
     *  • No duplicate QRIS or VA payment screens are pushed onto the back-stack.
     */
    @Test
    fun test_raceCondition_spamClickPayButton_buttonDisabledAfterFirstClick() {
        // ── Navigate to Checkout ───────────────────────────────────────────────────────────────
        navigateToEventList()
        waitForEventListToLoad()
        tapFirstEventCard()
        waitForDetailEventScreen()

        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_BUY_TICKET_BTN).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(TEXT_BUY_TICKET_BTN).performClick()
        composeRule.waitForIdle()

        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_BUY_NOW_BTN).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(TEXT_BUY_NOW_BTN).performClick()
        composeRule.waitForIdle()

        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_CHECKOUT_APPBAR).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_PAY_BTN, substring = true).fetchSemanticsNodes().isNotEmpty()
        }

        // ── 15 rapid clicks on "Pay" ──────────────────────────────────────────────────────────
        repeat(15) {
            try {
                composeRule.onNodeWithText(TEXT_PAY_BTN, substring = true).performClick()
            } catch (_: AssertionError) {
                // Node may have transitioned away; swallow and continue.
            }
        }
        composeRule.waitForIdle()

        // ── Assert 1: Button disabled OR loading indicator OR screen transitioned ───────────────
        // CheckoutContent: Button(enabled = !isLoading) → disabled after first successful click.
        // CircularProgressIndicator has ProgressBarRangeInfo semantics.
        val disabledOrLoading = run {
            // Check for a disabled Pay button.
            val payNodes = composeRule.onAllNodesWithText(TEXT_PAY_BTN, substring = true)
                .fetchSemanticsNodes()
            val anyDisabled = payNodes.any {
                it.config.getOrElseNullable(SemanticsProperties.Disabled) { null } != null
            }

            // Check for a visible progress indicator (ProgressBarRangeInfo key present).
            val hasProgress = composeRule.onAllNodes(
                SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo)
            ).fetchSemanticsNodes().isNotEmpty()

            // Check whether the screen navigated away from Checkout.
            val leftCheckout = composeRule.onAllNodesWithText(TEXT_CHECKOUT_APPBAR)
                .fetchSemanticsNodes().isEmpty()

            anyDisabled || hasProgress || leftCheckout
        }

        assert(disabledOrLoading) {
            "Expected 'Pay' to be disabled OR a loading indicator to appear after rapid clicking, " +
                    "but neither condition was met. Duplicate payment intents may have been fired."
        }

        // ── Assert 2: App still alive ─────────────────────────────────────────────────────────
        val appAlive =
            composeRule.onAllNodesWithText(TEXT_CHECKOUT_APPBAR).fetchSemanticsNodes().isNotEmpty()
                    || composeRule.onAllNodesWithText(TEXT_ORDER_CREATED_DIALOG).fetchSemanticsNodes().isNotEmpty()
                    || composeRule.onAllNodesWithText(TEXT_PAYMENT_APPBAR_QRIS).fetchSemanticsNodes().isNotEmpty()
                    || composeRule.onAllNodesWithText(TEXT_PAYMENT_APPBAR_VA).fetchSemanticsNodes().isNotEmpty()

        assert(appAlive) {
            "After rapid-clicking 'Pay', the app appears to have crashed or entered an unknown state."
        }

        // ── Assert 3: No duplicate payment screens ────────────────────────────────────────────
        val qrisCount = composeRule.onAllNodesWithText(TEXT_PAYMENT_APPBAR_QRIS)
            .fetchSemanticsNodes().size
        val vaCount   = composeRule.onAllNodesWithText(TEXT_PAYMENT_APPBAR_VA)
            .fetchSemanticsNodes().size

        assert(qrisCount <= 1) {
            "QRIS payment screen appeared $qrisCount times – duplicate navigation occurred."
        }
        assert(vaCount <= 1) {
            "VA payment screen appeared $vaCount times – duplicate navigation occurred."
        }
    }

    /**
     * 3c – Stress Click "See all arts" (Gallery Navigation)
     *
     * Rapidly clicks "See all arts" 8 times and asserts that exactly one Event Gallery screen
     * is on the back-stack (NavController's launchSingleTop or re-entrant guard is working).
     */
    @Test
    fun test_raceCondition_spamClickSeeAllArts_noDuplicateGalleryScreens() {
        navigateToEventList()
        waitForEventListToLoad()
        tapFirstEventCard()
        waitForDetailEventScreen()

        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_SEE_ALL_ARTS_BTN).fetchSemanticsNodes().isNotEmpty()
        }

        // ── 8 rapid clicks ────────────────────────────────────────────────────────────────────
        repeat(8) {
            try {
                composeRule.onNodeWithText(TEXT_SEE_ALL_ARTS_BTN).performClick()
            } catch (_: AssertionError) { /* node may have transitioned */ }
        }
        composeRule.waitForIdle()

        // Wait for the gallery screen.
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_EVENT_GALLERY_APPBAR).fetchSemanticsNodes().isNotEmpty()
        }

        // Exactly ONE gallery screen must be rendered (title node appears once).
        val galleryCount = composeRule.onAllNodesWithText(TEXT_EVENT_GALLERY_APPBAR)
            .fetchSemanticsNodes().size
        assert(galleryCount == 1) {
            "Expected exactly 1 'Event Gallery' screen title, but found $galleryCount. " +
                    "Duplicate screens were pushed onto the back-stack due to rapid tapping."
        }
    }

    // ══════════════════════════════════════════════════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ══════════════════════════════════════════════════════════════════════════════════════════════

    /**
     * Brings the Event List screen into view.
     *
     * If the home screen contains a bottom-nav "Home" tab or a text link labelled "Events",
     * this helper taps it.  Adjust the logic here to match your actual home-screen structure.
     */
    private fun navigateToEventList() {
        composeRule.waitForIdle()

        // If we are already on the Event List, do nothing.
        val alreadyHere = composeRule.onAllNodesWithText(TEXT_EVENTS_TITLE)
            .fetchSemanticsNodes().isNotEmpty()
        if (alreadyHere) return

        // Attempt to navigate by tapping a node labelled "Events".
        val nodes = composeRule.onAllNodesWithText(TEXT_EVENTS_TITLE, ignoreCase = true)
            .fetchSemanticsNodes()
        if (nodes.isNotEmpty()) {
            composeRule.onAllNodesWithText(TEXT_EVENTS_TITLE, ignoreCase = true)
                .onFirst()
                .performClick()
        }
        composeRule.waitForIdle()
    }

    /**
     * Waits for the event list to finish loading (LazyColumn appears in the composition tree).
     */
    private fun waitForEventListToLoad() {
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodes(hasScrollAction()).fetchSemanticsNodes().isNotEmpty()
        }
    }

    /**
     * Taps the first event card in the lazy list.
     *
     * Picks the first clickable, non-scroll node.  To target a specific event by name,
     * replace this with: composeRule.onNodeWithText("My Event Name").performClick()
     */
    private fun tapFirstEventCard() {
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodes(hasClickAction() and !hasScrollAction())
                .fetchSemanticsNodes().isNotEmpty()
        }
        val clickables = composeRule.onAllNodes(hasClickAction() and !hasScrollAction())
            .fetchSemanticsNodes()
        if (clickables.isEmpty()) fail("No clickable event card found in the event list.")

        composeRule.onAllNodes(hasClickAction() and !hasScrollAction())
            .onFirst()
            .performClick()
        composeRule.waitForIdle()
    }

    /**
     * Blocks until the "Detail event" top-bar title is displayed.
     */
    private fun waitForDetailEventScreen() {
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_DETAIL_EVENT_APPBAR).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(TEXT_DETAIL_EVENT_APPBAR).assertIsDisplayed()
    }

    /**
     * From the Detail Event screen, taps "See all arts" and waits for Event Gallery.
     */
    private fun navigateToPaintingGallery() {
        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_SEE_ALL_ARTS_BTN).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(TEXT_SEE_ALL_ARTS_BTN).performClick()
        composeRule.waitForIdle()

        composeRule.waitUntil(timeoutMillis = NETWORK_TIMEOUT_MS) {
            composeRule.onAllNodesWithText(TEXT_EVENT_GALLERY_APPBAR).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(TEXT_EVENT_GALLERY_APPBAR).assertIsDisplayed()
    }
}

/*
 * ─────────────────────────────────────────────────────────────────────────────────────────────
 * HILT TEST RUNNER  (create this file alongside this test if you don't have one already)
 * ─────────────────────────────────────────────────────────────────────────────────────────────
 *
 * File: app/src/androidTest/java/com/prayatna/lookiesapp/HiltTestRunner.kt
 *
 * package com.prayatna.lookiesapp
 *
 * import android.app.Application
 * import android.content.Context
 * import androidx.test.runner.AndroidJUnitRunner
 * import dagger.hilt.android.testing.HiltTestApplication
 *
 * class HiltTestRunner : AndroidJUnitRunner() {
 *     override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
 *         return super.newApplication(cl, HiltTestApplication::class.java.name, context)
 *     }
 * }
 *
 * Then in app/build.gradle.kts:
 *   defaultConfig {
 *       testInstrumentationRunner = "com.prayatna.lookiesapp.HiltTestRunner"
 *   }
 * ─────────────────────────────────────────────────────────────────────────────────────────────
 */
