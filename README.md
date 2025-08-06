# 📈 Bitcoin Price Tracker 💰

This is a simple and visual Android app that tracks real-time and historical Bitcoin prices using **Jetpack Compose**. It includes animated graphs, filter buttons, and fallback error handling — all structured with modern Android development practices.

## ✨ Features

- 🪙 **Live Bitcoin Price** — Updated from public API
- 📊 **Historical Price Chart** — With animation and filters (1D, 7D, 1M, etc.)
- 🎛️ **Filter Buttons** — Built with LazyHorizontalGrid for fluid UX
- 🔁 **Automatic UI refresh** with `LaunchedEffect` and coroutines
- ❌ **Error State Handling** — Graceful fallback and user feedback when API fails
- 🎨 **Color-coded Price Display** — Green when rising, Red when falling
- ⚙️ **Custom animation using Compose Charts**

## 📚 Libraries Used

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Retrofit](https://square.github.io/retrofit/)
- [Compose Charts (by @ehsannarmani)](https://github.com/ehsannarmani/Compose-Charts)  
  `implementation("io.github.ehsannarmani:compose-charts:0.1.7")`

## 🧠 What I Learned

- Creating dynamic and responsive UIs with Compose
- Handling multiple API calls with isolated error states
- Visualizing data with animated charts
- Managing UI state using `remember`, `mutableStateOf`, and `LaunchedEffect`

## 🔧 Setup

Make sure you have:

- Android Studio **Hedgehog** or later
- JDK **21**
- Minimum SDK **21**

Then just clone and run:

```bash
git clone https://github.com/alanliongar/BitcoinPrice
