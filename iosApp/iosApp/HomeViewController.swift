//
//  HomeViewController.swift
//  iosApp
//
//  Created by syxc on 2024/5/9.
//  Copyright © 2024 jithub.com. All rights reserved.
//

import SwiftUI
import UIKit

class HomeViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()

        // 1.
        let vc = UIHostingController(rootView: ContentView())

        let swiftuiView = vc.view!
        swiftuiView.translatesAutoresizingMaskIntoConstraints = false

        // 2.Add the view controller to the destination view controller.
        addChild(vc)
        view.addSubview(swiftuiView)

        // 3.Create and activate the constraints for the swiftui's view.
        NSLayoutConstraint.activate([
            swiftuiView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            swiftuiView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            swiftuiView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            swiftuiView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor),
        ])

        // 4.Notify the child view controller that the move is complete.
        vc.didMove(toParent: self)
    }
}
