<script setup lang="ts">


</script>

<template>
  <div>

    <Main only-father-container>
      <template #banner>
        <Banner title="视频" subtitle="video"/>
      </template>
      <template #content>
        <div class="content">
          <div class="header">
            <div class="essay_title">
        <el-divider border-style="dashed" content-position="left">
          <div>
            <SvgIcon name="random_essay" color="#409EFF" class="icon"/>
            <span>视频</span>
          </div>
        </el-divider>
      </div>
          </div>
          <el-divider/>
          <div>
        <CardVideo/>
      </div>
      <div>
        <Pagination/>
      </div>

        </div>
      </template>
    </Main>
  </div>
</template>

<style scoped lang="scss">
@import "@/styles/mixin.scss";

:deep(.el-dialog__body){
  padding-top: 0;
}

:deep(.el-dialog){
  // 过渡时间
  transition: all 0.3s ease-in-out;
  @media (max-width: 1000px)  {
    width: 60%;
  }

  @media (max-width: 550px)  {
    width: 90%;
  }
}

.content {
  margin-top: 1.5rem;

  .link {
    display: flex;
    flex-wrap: wrap;

    .item {
      margin: 0.5rem;
      width: calc(100% / 3 - 1rem);
      height: 13rem;
      border: #0072ff 1px solid;
      border-radius: $border-radius;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
      @include flex;
      flex-direction: column;
      overflow: hidden;
      transition: all 0.3s ease-in-out;

      @media screen and (max-width: 830px) {
        width: calc(100% / 2 - 1rem);
      }

      @media screen and (max-width: 580px) {
        width: calc(100% - 1rem);

      }

      &:hover {
        .content_link {
          height: 50%;
          background: #0072ff;

          .name {
            color: #fdeeee;
          }

          .description {
            color: #fdeeee;
          }
        }

        .bg {
          filter: blur(2px);
        }
      }

      .bg {
        background-size: cover !important;
        background-position: center !important;
        width: 100%;
        height: 65%;
      }

      .content_link {
        transition: all 0.3s ease-in-out;
        display: flex;
        flex-direction: column;
        align-items: center;
        //background: white;
        height: 35%;
        width: 100%;
        padding: 0.5rem 0;

        div {
          display: flex;
          align-items: center;
          justify-content: center;

          .el-avatar {
            width: 2.5rem;
            height: 2.5rem;
            margin-left: -3rem;
          }

          .name {
            font-size: 1rem;
            font-weight: bold;
            margin-left: 0.5rem;

            a {
              // 去掉a标签样式
              color: inherit;
              text-decoration: none;
              cursor: pointer;
            }
          }
        }

        .description {
          line-height: 1rem;
          width: 15rem;
          font-size: 0.85rem;
          margin-left: 0.5rem;
          margin-top: 0.5rem;
          color: #7C7C7C;
        }
      }
    }
  }

  .title_content {
    font-weight: bold;
    font-size: 0.8rem;
    color: #999;
    display: flex;
    flex-direction: column;
    background: var(--mao-bg-message);
    padding: 0.5rem;
    border-radius: $border-radius;
    margin-bottom: 1rem;

    span {
      margin-bottom: 1rem;
      line-height: 1rem;
    }
  }

  .header {
    display: flex;
    justify-content: space-between;

    .el-button {
      height: 2.5rem;
    }

    .title {
      font-size: 2rem;
    }
  }
}
</style>