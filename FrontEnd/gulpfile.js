var gulp = require('gulp');
var sourcemaps = require('gulp-sourcemaps');
var tsc = require('gulp-typescript');
var tslint = require('gulp-tslint');
var tsProject = tsc.createProject('tsconfig.json');
var config = require('./gulp.config')();
const del = require('del');

var browserSync = require('browser-sync');
var superstatic = require('superstatic');

gulp.task('clean', function () {
  return del('dist/**/*');
});

gulp.task('ts-lint', function () {
    return gulp.src(config.allTs)
        .pipe(tslint())
        .pipe(tslint.report('prose', {
            emitError: false
        }))
});

gulp.task('compile-ts', function () {
    var sourceTsFiles = [
        config.allTs,
        config.typings
    ];

    var tsResult = gulp
        .src(sourceTsFiles)
        .pipe(sourcemaps.init())
        .pipe(tsc(tsProject));

    return tsResult
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest(config.tsOutputPath));
});

gulp.task('compile', ['clean', 'compile-ts'], function () {
  return gulp
    .pipe(gulp.dest('dist'));
});

gulp.task('serve', ['ts-lint', 'compile-ts'], function () {
    gulp.watch([config.allTs], ['ts-lint', 'compile-ts']).on("change", browserSync.reload);

    browserSync({
        port: 3000,
        file: ['index.html', 'scripts/**/*.js', 'styles/**/*.css'],
        injectChanges: true,
        logFileChanges: false,
        logLevel: 'silent',
        notify: true,
        reloadDelay: 0,
        server: {
            baseDir: './',
            middleware: superstatic({debug: false})
        }
    });
    
});

gulp.task('build', ['compile']);
gulp.task('default', ['serve']);
