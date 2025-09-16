const verifyConditions = `
git status
git rev-parse --abbrev-ref HEAD
`

const prepareCmd = `
# Update 'version' value in gradle.properties file
sed -i '/^version/d' gradle.properties
echo version=\${nextRelease.version} >> gradle.properties
# Update .env file
echo VERSION="\${nextRelease.version}" > .env
echo PROJECT_NAME=$(grep -Po 'rootProject\\s*\\.\\s*name\\s*=\\s*"\\K[\\w-]+(?=")' settings.gradle.kts) >> .env
`

const publishCmd = `
git add gradle.properties .env
git commit -m "chore(release): update gradle.properties .env versions to \${nextRelease.version} [skip ci]"
git push --set-upstream origin master
`
import config from 'semantic-release-preconfigured-conventional-commits' with {type: 'json'};

// config.branches = []
// config.branches.push("master", "main")
config.plugins.push(
    ["@semantic-release/exec", {
        "verifyConditionsCmd": verifyConditions,
        "prepareCmd": prepareCmd,
        "publishCmd": publishCmd,
        "verifyReleaseCmd": "echo ${nextRelease.version} > .next-version",
    }],
    ["@semantic-release/github", {
        "assets": [
            { "path": "build/packages/**" },
            { "path": "build/shadow/**" },
        ]
    }],
    "@semantic-release/git",
)
export default config
