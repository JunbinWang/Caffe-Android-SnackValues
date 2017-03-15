var formidable = require('formidable');
var path= require('path');
const http = require('http');
const fs = require('fs');
const util = require('util');
const querystring =require('querystring');

//用http模块创建一个http服务端 
http.createServer(function(req, res) {
  if (req.url == '/upload' && req.method.toLowerCase() === 'get'){
  	//显示一个用于文件上传的form
	res.writeHead(200, {'content-type': 'text/html'});
	res.end(
	  '<form action="/upload" enctype="multipart/form-data" method="post">'+
	    '<input type="file" name="upload" multiple="multiple" />'+
	    '<input type="submit" value="Upload" />'+
	  '</form>'
	);
  } else{
    console.log("HELLO!");
      var form = new formidable.IncomingForm();
      form.uploadDir = path.join(__dirname, 'tmp'); //文件保存在系统临时目录

      form.maxFieldsSize = 5 * 1024 * 1024;  //上传文件大小限制为最大5M
      form.keepExtensions = true;        //使用文件的原扩展名

      form.parse(req, function (err, fields, file) {
          var filePath = '';
          for(var key in file){
              if( file[key].path && filePath==='' ){
                  filePath = file[key].path;
                  break;
              }
          }

          //文件移动的目录文件夹，不存在时创建目标文件夹
          var targetDir = path.join(__dirname, 'upload');
          if (!fs.existsSync(targetDir)) {
              fs.mkdir(targetDir);
          }
          var fileExt = filePath.substring(filePath.lastIndexOf('.')).toLowerCase();
          var fileName = new Date().getTime() + fileExt;
          var targetFile = path.join(targetDir, fileName);
          //移动文件
          fs.rename(filePath, targetFile, function (err) {
              if (err) {
                  console.info(err);
                  res.writeHead(404, {'content-type': 'text/html'});
                  res.end('error');
              } else {
                  //上传成功，返回文件的相对路径
                  var fileUrl = '/upload/' + fileName;
                  res.writeHead(200, {'content-type': 'text/html'});
                  res.end('success');
              }
          });

          var fWriteName = path.join(__dirname,"label.txt");
          var label = fields.labelName;
          fs.appendFile(fWriteName, label+' '+fileName+'\n', function (err) {
          });
      });



  }
    }).listen(3000);
