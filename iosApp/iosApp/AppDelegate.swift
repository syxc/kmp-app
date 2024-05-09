//
//  AppDelegate.swift
//  iosApp
//
//  Created by syxc on 2024/5/9.
//  Copyright © 2024 github.com. All rights reserved.
//

import shared
import UIKit

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
  var window: UIWindow?

  func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
    // Override point for customization after application launch.

    UITabBarItem.appearance().setTitleTextAttributes(
      [NSAttributedString.Key.font: UIFont.systemFont(ofSize: 15)], for: .normal
    )

    let homeViewController = HomeViewController()
    homeViewController.title = "SwiftUI"

    let mokoViewController = MokoViewController()
    mokoViewController.title = "Moko Resource"

    let anotherViewController = CounterViewController()
    // anotherViewController.view.backgroundColor = .white
    anotherViewController.title = "Redwood UI"

    // Set up the UITabBarController
    let tabBarController = UITabBarController()
    tabBarController.viewControllers = [
      // Wrap them in a UINavigationController for the titles
      UINavigationController(rootViewController: homeViewController),
      UINavigationController(rootViewController: mokoViewController),
      UINavigationController(rootViewController: anotherViewController),
    ]
    tabBarController.tabBar.items?[0].title = "Home"
    tabBarController.tabBar.items?[1].title = "Moko"
    tabBarController.tabBar.items?[2].title = "Redwood"

    // Set the tab bar controller as the window's root view controller and make it visible
    window = UIWindow(frame: UIScreen.main.bounds)
    window?.backgroundColor = .white
    window?.rootViewController = tabBarController
    window?.makeKeyAndVisible()

    return true
  }

  func applicationWillResignActive(_ application: UIApplication) {}

  func applicationDidEnterBackground(_ application: UIApplication) {}

  func applicationWillEnterForeground(_ application: UIApplication) {}

  func applicationDidBecomeActive(_ application: UIApplication) {}

  func applicationWillTerminate(_ application: UIApplication) {}
}

extension KotlinThrowable: Swift.Error {}
