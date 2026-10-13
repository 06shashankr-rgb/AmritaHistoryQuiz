#!/bin/sh
# Gradle wrapper launcher. Requires the standard gradle-wrapper.jar generated
# by Android Studio/Gradle in gradle/wrapper/gradle-wrapper.jar.

APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd -P) || exit 1
if [ -n "$JAVA_HOME" ]; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi

if [ ! -f "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "Gradle wrapper JAR is missing. Open the project in Android Studio or generate the wrapper with Gradle." >&2
    exit 1
fi

exec "$JAVACMD" -Xmx64m -Xms64m -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
