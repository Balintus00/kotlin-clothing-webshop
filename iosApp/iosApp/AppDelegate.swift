//
//  AppDelegate.swift
//  Kotlin Clothing Webshop
//
//  Created by user on 2024. 05. 05..
//  Copyright © 2024. hu.bme.aut.ixnoyb. All rights reserved.
//

import client
import SwiftUI

class AppDelegate : NSObject, UIApplicationDelegate {
    
    let component: RootComponent = DefaultRootComponent(
            componentContext: DefaultComponentContext(
                lifecycle: ApplicationLifecycle()
            ),
            storeFactory: LoggingStoreFactory(
                delegate: DefaultStoreFactory()
            )
        )
}
