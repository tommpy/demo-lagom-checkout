lazy val project_template = (project in file("."))
  .aggregate(
    common,
    exercise_000_initial_state,
    exercise_010_basket_service,
    exercise_020_basket_entity,
    exercise_021_get_basket_price,
    exercise_030_place_order,
    exercise_040_order_service
 )
  .settings(CommonSettings.commonSettings: _*)

lazy val common = project
  .settings(CommonSettings.commonSettings: _*)

lazy val exercise_000_initial_state = project
  .settings(CommonSettings.commonSettings: _*)
  .dependsOn(common % "test->test;compile->compile")

lazy val exercise_010_basket_service = project
  .settings(CommonSettings.commonSettings: _*)
  .dependsOn(common % "test->test;compile->compile")

lazy val exercise_020_basket_entity = project
  .settings(CommonSettings.commonSettings: _*)
  .dependsOn(common % "test->test;compile->compile")

lazy val exercise_021_get_basket_price = project
  .settings(CommonSettings.commonSettings: _*)
  .dependsOn(common % "test->test;compile->compile")

lazy val exercise_030_place_order = project
  .settings(CommonSettings.commonSettings: _*)
  .dependsOn(common % "test->test;compile->compile")

lazy val exercise_040_order_service = project
  .settings(CommonSettings.commonSettings: _*)
  .dependsOn(common % "test->test;compile->compile")
