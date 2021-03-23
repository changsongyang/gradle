package projects

import common.VersionedSettingsBranch
import jetbrains.buildServer.configs.kotlin.v2019_2.Project
import model.CIBuildModel
import model.JsonBasedGradleSubprojectProvider
import promotion.PromotionProject
import util.UtilPerformanceProject
import util.UtilProject
import java.io.File

class GradleBuildToolRootProject(branch: VersionedSettingsBranch) : Project({
    val model = CIBuildModel(
        projectId = "Check",
        branch = branch,
        buildScanTags = listOf("Check"),
        subprojects = JsonBasedGradleSubprojectProvider(File("./subprojects.json"))
    )
    subProject(CheckProject(model))

    subProject(PromotionProject(model.branch))
    subProject(UtilProject)
    subProject(UtilPerformanceProject)
})
