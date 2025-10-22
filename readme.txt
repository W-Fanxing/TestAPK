这是一个测试apk，用于测试pendingIntent

创建过程：
1. 本地在Android Studio中创建一个空activity
2. 让AI写3个主要文件：MainActivity.java    AndroidManifest.xml    activity_main.xml
3. 让AS自己gradle
4. 有问题的地方搜搜，或者参考stabilitytest.apk
注意：安装apk后，adb reboot，adb root，apk中Log才有效

第一次提交，将本地apk项目上传到自己的github：
# 本地项目根目录下执行
git init
git add .
git commit -m "Initial commit: Android project"
git remote add origin https://github.com/W-Fanxing/TestAPK.git
git branch -M main   重命名分支为main
git push -u origin main     username输入W-Fanxing，password输入<生成的token>
--- 推送完成 ---

修改之后提交：
# 直接修改了github需要先pull
git pull
git config pull.rebase false
# 本地修改可以提交
git add .
git commit -m "..."
git push -u origin main  # username输入W-Fanxing，password输入<token>，过期可以重新生成


Token生成方法:
# 创建Personal Access Token
1. 登录GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. 点击 Generate new token → Generate new token (classic)
3. 填写信息：
   Note: My Local Machine（描述性名称）
   Expiration: 选择有效期（建议90天或1年）
   Scopes: 至少勾选 repo（完整仓库权限）
4. 点击 Generate token（注意：要立即复制生成的token，因为只显示一次）
PS：使用Token进行推送
    bash
    # 当提示输入密码时，粘贴token而不是密码
    git push -u origin main
    # 或者直接使用token在URL中
    git remote set-url origin https://<token>@github.com/W-Fanxing/TestAPK.git