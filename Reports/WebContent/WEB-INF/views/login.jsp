<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en" dir="ltr">
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<meta http-equiv="Content-Language" content="en" />
<meta name="msapplication-TileColor" content="#2d89ef">
<meta name="theme-color" content="#4188c9">
<meta name="apple-mobile-web-app-status-bar-style"
	content="black-translucent" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="mobile-web-app-capable" content="yes">
<meta name="HandheldFriendly" content="True">
<meta name="MobileOptimized" content="320">
<link rel="icon" href="./favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" type="image/x-icon" href="./favicon.ico" />
<!-- Generated: 2019-04-04 16:55:45 +0200 -->
<title>Login Page Ht Reports</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,300i,400,400i,500,500i,600,600i,700,700i&amp;subset=latin-ext">
<script src="./assets/js/require.min.js"></script>
<script>
	requirejs.config({
		baseUrl : '.'
	});
</script>
<!-- Dashboard Core -->
<link href="./assets/css/dashboard.css" rel="stylesheet" />
<script src="./assets/js/dashboard.js"></script>
<!-- c3.js Charts Plugin -->
<link href="./assets/plugins/charts-c3/plugin.css" rel="stylesheet" />
<script src="./assets/plugins/charts-c3/plugin.js"></script>
<!-- Google Maps Plugin -->
<link href="./assets/plugins/maps-google/plugin.css" rel="stylesheet" />
<script src="./assets/plugins/maps-google/plugin.js"></script>
<!-- Input Mask Plugin -->
<script src="./assets/plugins/input-mask/plugin.js"></script>
<!-- Datatables Plugin -->
<script src="./assets/plugins/datatables/plugin.js"></script>
</head>
<body class="">
	<div class="page">
		<div class="page-single">
			<div class="container">
				<div class="row">
					<div class="col col-login mx-auto">
						<div class="text-center mb-6">
							<img src="./demo/brand/tabler.svg" class="h-6" alt="">
						</div>
						<c:if test="${errorMessage  ne null}">
							<h2 style="border: 2px solid cyan; color: red; width: 200px;">
								<c:out value="${errorMessage}" />
							</h2>
						</c:if>
						<form class="card" action="login" method="post">
							<div class="card-body p-6">
								<div class="card-title">Login to your account</div>
								<div class="form-group">
									<label class="form-label">User Name</label> <input type="text"
										class="form-control" id="exampleInputEmail1"
										aria-describedby="emailHelp" placeholder="Enter email"
										name="username" required="required">
								</div>
								<div class="form-group">
									<label class="form-label"> Password <a
										href="./forgot-password.html" class="float-right small">I
											forgot password</a>
									</label> <input type="password" class="form-control"
										id="exampleInputPassword1" placeholder="Password"
										name="password" required="required">
								</div>
								<div class="form-group">
									<label class="custom-control custom-checkbox"> <input
										type="checkbox" class="custom-control-input" /> <span
										class="custom-control-label">Remember me</span>
									</label>
								</div>
								<div class="form-footer">
									<button type="submit" class="btn btn-primary btn-block">Sign
										in</button>
								</div>
							</div>
							<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" />
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>